package org.resume.paymentservice.service.payment;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.resume.paymentservice.exception.NotFoundException;
import org.resume.paymentservice.model.dto.PaymentCreationData;
import org.resume.paymentservice.model.entity.Payment;
import org.resume.paymentservice.model.entity.User;
import org.resume.paymentservice.model.enums.PaymentStatus;
import org.resume.paymentservice.repository.PaymentRepository;
import org.resume.paymentservice.service.user.UserService;
import org.resume.paymentservice.utils.ErrorMessages;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final UserService userService;

    public Payment savePayment(PaymentCreationData data) {
        User user = userService.getUserById(data.getUserId());

        Payment payment = new Payment(
                data.getStripePaymentIntentId(),
                data.getAmount(),
                data.getCurrency(),
                PaymentStatus.PENDING,
                data.getDescription(),
                data.getClientSecret(),
                user,
                null // TODO: implement saved card support
        );

        return paymentRepository.save(payment);
    }

    @Transactional
    public void updatePaymentStatus(String stripePaymentIntentId, PaymentStatus newStatus) {
        Payment payment = findByStripePaymentIntentId(stripePaymentIntentId);
        PaymentStatus oldStatus = payment.getStatus();
        payment.setStatus(newStatus);
        paymentRepository.save(payment);

        log.info("Payment status updated: stripeId={}, {} -> {}", stripePaymentIntentId, oldStatus, newStatus);
    }

    public Payment findByStripePaymentIntentId(String stripePaymentIntentId) {
        return paymentRepository.findByStripePaymentIntentId(stripePaymentIntentId)
                .orElseThrow(() -> NotFoundException.paymentByStripeId(stripePaymentIntentId));
    }

    public Payment findByStripePaymentIntentIdAndUser(String stripePaymentIntentId, User user) {
        Payment payment = findByStripePaymentIntentId(stripePaymentIntentId);

        if (!payment.getUser().getId().equals(user.getId())) {
            throw new AccessDeniedException(ErrorMessages.PAYMENT_ACCESS_DENIED);
        }
        return payment;
    }

}
