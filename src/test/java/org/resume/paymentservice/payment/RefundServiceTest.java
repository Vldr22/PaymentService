package org.resume.paymentservice.payment;

import org.instancio.Instancio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.resume.paymentservice.exception.AlreadyExistsException;
import org.resume.paymentservice.exception.NotFoundException;
import org.resume.paymentservice.exception.PaymentException;
import org.resume.paymentservice.model.entity.Payment;
import org.resume.paymentservice.model.entity.Refund;
import org.resume.paymentservice.model.entity.Staff;
import org.resume.paymentservice.model.enums.PaymentStatus;
import org.resume.paymentservice.model.enums.RefundReason;
import org.resume.paymentservice.model.enums.RefundStatus;
import org.resume.paymentservice.model.entity.User;
import org.resume.paymentservice.repository.RefundRepository;
import org.resume.paymentservice.service.payment.RefundService;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.instancio.Select.field;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("RefundService — операции с возвратами платежей")
class RefundServiceTest {

    private static final String STRIPE_REFUND_ID = "re_test_123";
    private static final String STRIPE_PAYMENT_ID = "pi_existing_123";
    private static final String NONEXISTENT_REFUND = "pi_nonexistent_999";

    @Mock
    private RefundRepository refundRepository;

    @InjectMocks
    private RefundService refundService;

    private Payment succeededPayment;
    private User user;
    private Staff reviewer;

    @BeforeEach
    void setUp() {
        user = Instancio.create(User.class);
        reviewer = Instancio.create(Staff.class);

        succeededPayment = Instancio.of(Payment.class)
                .set(field(Payment::getStatus), PaymentStatus.SUCCEEDED)
                .set(field(Payment::getStripePaymentIntentId), STRIPE_PAYMENT_ID)
                .create();
    }

    // createRefundRequest

    /**
     * Проверяет успешное создание запроса на возврат для платежа со статусом SUCCEEDED.
     * Возврат должен сохраниться со статусом PENDING.
     */
    @Test
    void shouldCreateRefundRequest_whenPaymentSucceeded() {
        when(refundRepository.existsByPaymentIdAndStatusIn(any(), any())).thenReturn(false);
        when(refundRepository.save(any(Refund.class))).thenAnswer(inv -> inv.getArgument(0));

        Refund result = refundService.createRefundRequest(succeededPayment, user, RefundReason.DUPLICATE);

        assertThat(result.getStatus()).isEqualTo(RefundStatus.PENDING);
        assertThat(result.getPayment()).isEqualTo(succeededPayment);
        assertThat(result.getUser()).isEqualTo(user);
        verify(refundRepository).save(any(Refund.class));
    }

    /**
     * Проверяет, что нельзя создать возврат для платежа не в статусе SUCCEEDED.
     * Например, PENDING или FAILED платёж не должен допускать refund.
     */
    @Test
    void shouldThrowPaymentException_whenPaymentNotSucceeded() {
        Payment pendingPayment = Instancio.of(Payment.class)
                .set(field(Payment::getStatus), PaymentStatus.PENDING)
                .create();

        assertThatThrownBy(() ->
                refundService.createRefundRequest(pendingPayment, user, RefundReason.DUPLICATE)
        ).isInstanceOf(PaymentException.class);
    }

    /**
     * Проверяет защиту от дублирующих запросов на возврат.
     * Если для платежа уже есть PENDING или APPROVED refund — бросаем исключение.
     */
    @Test
    void shouldThrowAlreadyExists_whenDuplicateRefundRequest() {
        when(refundRepository.existsByPaymentIdAndStatusIn(any(), any())).thenReturn(true);

        assertThatThrownBy(() ->
                refundService.createRefundRequest(succeededPayment, user, RefundReason.DUPLICATE)
        ).isInstanceOf(AlreadyExistsException.class);
    }

    // findById

    /**
     * Возвращает refund по существующему ID.
     */
    @Test
    void shouldReturnRefund_whenIdExists() {
        Refund refund = Instancio.create(Refund.class);
        when(refundRepository.findById(refund.getId())).thenReturn(Optional.of(refund));

        Refund result = refundService.findById(refund.getId());

        assertThat(result).isEqualTo(refund);
    }

    /**
     * Бросает NotFoundException если refund с таким ID не существует.
     */
    @Test
    void shouldThrowNotFound_whenRefundIdNotExist() {
        when(refundRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> refundService.findById(99L))
                .isInstanceOf(NotFoundException.class);
    }

    // findPendingRefunds

    /**
     * Проверяет что метод возвращает список возвратов в статусе PENDING.
     */
    /**
     * Проверяет что метод возвращает список возвратов в статусе PENDING.
     */
    @Test
    void shouldReturnPendingRefunds_withCorrectContent() {
        List<Refund> pending = List.of(
                Instancio.of(Refund.class).set(field(Refund::getStatus), RefundStatus.PENDING).create(),
                Instancio.of(Refund.class).set(field(Refund::getStatus), RefundStatus.PENDING).create()
        );

        when(refundRepository.findByStatus(RefundStatus.PENDING)).thenReturn(pending);

        List<Refund> result = refundService.findPendingRefunds();

        assertThat(result)
                .hasSize(2)
                .isEqualTo(pending)
                .allMatch(r -> r.getStatus() == RefundStatus.PENDING);
    }

    // approveRefund

    /**
     * Проверяет, что после одобрения возврата устанавливается статус APPROVED,
     * привязывается Stripe refund ID и сохраняется информация о ревьюере.
     */
    @Test
    void shouldApproveRefund_andSetReviewerAndStripeId() {
        Refund refund = Instancio.of(Refund.class)
                .set(field(Refund::getStatus), RefundStatus.PENDING)
                .create();

        refundService.approveRefund(refund, reviewer, STRIPE_REFUND_ID);

        assertThat(refund.getStatus()).isEqualTo(RefundStatus.APPROVED);
        assertThat(refund.getStripeRefundId()).isEqualTo(STRIPE_REFUND_ID);
        assertThat(refund.getReviewedBy()).isEqualTo(reviewer);
        verify(refundRepository).save(refund);
    }

    // rejectRefund

    /**
     * Проверяет, что после отклонения возврата устанавливается статус REJECTED
     * и сохраняется ревьюер. Stripe при этом не вызывается.
     */
    @Test
    void shouldRejectRefund_andSetReviewer() {
        Refund refund = Instancio.of(Refund.class)
                .set(field(Refund::getStatus), RefundStatus.PENDING)
                .create();

        refundService.rejectRefund(refund, reviewer);

        assertThat(refund.getStatus()).isEqualTo(RefundStatus.REJECTED);
        assertThat(refund.getReviewedBy()).isEqualTo(reviewer);
        verify(refundRepository).save(refund);
    }

    // updateRefundStatusByPaymentIntentId

    /**
     * Проверяет обновление статуса возврата по Stripe payment intent ID.
     * Используется при обработке webhook событий от Stripe.
     */
    @Test
    void shouldUpdateRefundStatus_whenApprovedRefundFound() {
        Refund refund = Instancio.of(Refund.class)
                .set(field(Refund::getStatus), RefundStatus.APPROVED)
                .create();

        when(refundRepository.findByPaymentStripePaymentIntentIdAndStatus(STRIPE_PAYMENT_ID, RefundStatus.APPROVED))
                .thenReturn(Optional.of(refund));

        refundService.updateRefundStatusByPaymentIntentId(STRIPE_PAYMENT_ID, RefundStatus.SUCCEEDED);

        assertThat(refund.getStatus()).isEqualTo(RefundStatus.SUCCEEDED);
        verify(refundRepository).save(refund);
    }

    /**
     * Бросает NotFoundException если не найден APPROVED refund для данного payment intent.
     */
    @Test
    void shouldThrowNotFound_whenNoApprovedRefundForPaymentIntent() {
        when(refundRepository.findByPaymentStripePaymentIntentIdAndStatus(NONEXISTENT_REFUND, RefundStatus.APPROVED))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() ->
                refundService.updateRefundStatusByPaymentIntentId(NONEXISTENT_REFUND, RefundStatus.SUCCEEDED)
        ).isInstanceOf(NotFoundException.class);
    }

    // validatePendingStatus

    /**
     * Не бросает исключений если refund в статусе PENDING.
     */
    @Test
    void shouldPassValidation_whenRefundIsPending() {
        Refund refund = Instancio.of(Refund.class)
                .set(field(Refund::getStatus), RefundStatus.PENDING)
                .create();

        refundService.validatePendingStatus(refund);
    }

    /**
     * Бросает PaymentException если refund не в статусе PENDING.
     */
    @Test
    void shouldThrowPaymentException_whenRefundNotPending() {
        Refund refund = Instancio.of(Refund.class)
                .set(field(Refund::getStatus), RefundStatus.APPROVED)
                .create();

        assertThatThrownBy(() -> refundService.validatePendingStatus(refund))
                .isInstanceOf(PaymentException.class);
    }
}
