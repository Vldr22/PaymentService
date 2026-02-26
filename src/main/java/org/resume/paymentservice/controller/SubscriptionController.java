package org.resume.paymentservice.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.resume.paymentservice.model.dto.CommonResponse;
import org.resume.paymentservice.model.dto.request.CreateSubscriptionRequest;
import org.resume.paymentservice.model.dto.response.BillingAttemptResponse;
import org.resume.paymentservice.model.dto.response.SubscriptionResponse;
import org.resume.paymentservice.service.facade.SubscriptionFacadeService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/subscriptions")
public class SubscriptionController {

    private final SubscriptionFacadeService subscriptionFacadeService;


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CommonResponse<SubscriptionResponse> createSubscription(
            @Valid @RequestBody CreateSubscriptionRequest request
    ) {
        SubscriptionResponse response = subscriptionFacadeService.createSubscription(request);
        return CommonResponse.success(response);
    }

    @GetMapping
    public CommonResponse<SubscriptionResponse> getSubscription() {
        SubscriptionResponse response = subscriptionFacadeService.getSubscription();
        return CommonResponse.success(response);
    }

    @PatchMapping("/cancel")
    public CommonResponse<SubscriptionResponse> cancelSubscription() {
        SubscriptionResponse response = subscriptionFacadeService.cancelSubscription();
        return CommonResponse.success(response);
    }

    @GetMapping("/billing-history")
    public CommonResponse<List<BillingAttemptResponse>> getBillingHistory() {
        List<BillingAttemptResponse> response = subscriptionFacadeService.getBillingHistory();
        return CommonResponse.success(response);
    }
}
