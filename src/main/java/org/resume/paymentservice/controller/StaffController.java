package org.resume.paymentservice.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.resume.paymentservice.model.dto.CommonResponse;
import org.resume.paymentservice.model.dto.request.ChangePasswordRequest;
import org.resume.paymentservice.service.facade.AuthFacadeService;
import org.resume.paymentservice.utils.SuccessMessages;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/staff")
public class StaffController {

    private final AuthFacadeService authFacadeService;

    @PatchMapping("/password")
    public CommonResponse<String> changePassword(
            @Valid @RequestBody ChangePasswordRequest request
    ) {
        authFacadeService.changePassword(request);
        return CommonResponse.success(SuccessMessages.UPDATE_PASSWORD_SUCCESS);
    }

}
