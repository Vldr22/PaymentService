package org.resume.paymentservice.facade;

import org.instancio.Instancio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.resume.paymentservice.model.dto.response.RefundDetailResponse;
import org.resume.paymentservice.model.entity.Payment;
import org.resume.paymentservice.model.entity.Refund;
import org.resume.paymentservice.model.entity.Staff;
import org.resume.paymentservice.model.entity.User;
import org.resume.paymentservice.model.enums.RefundStatus;
import org.resume.paymentservice.service.facade.SupportFacadeService;
import org.resume.paymentservice.service.payment.RefundService;
import org.resume.paymentservice.service.payment.StripeService;
import org.resume.paymentservice.service.user.StaffService;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.instancio.Select.field;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("SupportFacadeService — обработка возвратов сотрудниками поддержки")
class SupportFacadeServiceTest {

    private static final String STRIPE_REFUND_ID = "re_test_123";

    @Mock
    private RefundService refundService;

    @Mock
    private StripeService stripeService;

    @Mock
    private StaffService staffService;

    @InjectMocks
    private SupportFacadeService supportFacadeService;

    private Staff reviewer;
    private Refund refund;

    @BeforeEach
    void setUp() {
        reviewer = Instancio.create(Staff.class);

        User client = Instancio.create(User.class);
        Payment payment = Instancio.create(Payment.class);

        refund = Instancio.of(Refund.class)
                .set(field(Refund::getUser), client)
                .set(field(Refund::getPayment), payment)
                .set(field(Refund::getStatus), RefundStatus.PENDING)
                .create();
    }

    // getPendingRefunds

    /**
     * Возвращает список возвратов в статусе PENDING,
     * смапленных в RefundDetailResponse.
     */
    @Test
    void shouldReturnPendingRefunds_asMappedResponseList() {
        when(refundService.findPendingRefunds()).thenReturn(List.of(refund));

        List<RefundDetailResponse> result = supportFacadeService.getPendingRefunds();

        assertThat(result).hasSize(1);
        assertThat(result.getFirst().refundId()).isEqualTo(refund.getId());
    }

    // approveRefund

    /**
     * Проверяет одобрение возврата — статус валидируется, возврат создаётся в Stripe,
     * и refund помечается как APPROVED с привязкой Stripe ID.
     */
    @Test
    void shouldApproveRefund_andCallStripe() {
        when(staffService.getCurrentStaff()).thenReturn(reviewer);
        when(refundService.findById(refund.getId())).thenReturn(refund);
        when(stripeService.createRefund(any(), any())).thenReturn(STRIPE_REFUND_ID);

        RefundDetailResponse result = supportFacadeService.approveRefund(refund.getId());

        assertThat(result).isNotNull();
        verify(refundService).validatePendingStatus(refund);
        verify(stripeService).createRefund(
                refund.getPayment().getStripePaymentIntentId(),
                refund.getReason()
        );
        verify(refundService).approveRefund(refund, reviewer, STRIPE_REFUND_ID);
    }

    // rejectRefund

    /**
     * Проверяет отклонение возврата — статус валидируется,
     * Stripe не вызывается, refund помечается как REJECTED.
     */
    @Test
    void shouldRejectRefund_withoutCallingStripe() {
        when(staffService.getCurrentStaff()).thenReturn(reviewer);
        when(refundService.findById(refund.getId())).thenReturn(refund);

        RefundDetailResponse result = supportFacadeService.rejectRefund(refund.getId());

        assertThat(result).isNotNull();
        verify(refundService).validatePendingStatus(refund);
        verify(refundService).rejectRefund(refund, reviewer);
    }
}
