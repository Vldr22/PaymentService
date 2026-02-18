package org.resume.paymentservice.controller;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.resume.paymentservice.model.dto.CommonResponse;
import org.resume.paymentservice.model.dto.request.*;
import org.resume.paymentservice.model.dto.response.ClientResponse;
import org.resume.paymentservice.model.dto.response.TokenResponse;
import org.resume.paymentservice.service.facade.AuthFacadeService;
import org.resume.paymentservice.utils.SuccessMessages;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import static org.resume.paymentservice.contants.SecurityConstants.COOKIE_NAME;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthFacadeService authFacadeService;

    // ======== CLIENT ========
    @PostMapping("/register-client")
    @ResponseStatus(HttpStatus.CREATED)
    public CommonResponse<ClientResponse> registerClient(
            @Valid @RequestBody ClientRegistrationRequest request
    ) {
        ClientResponse response = authFacadeService.registerClient(request);
        return CommonResponse.success(response);
    }

    @PostMapping("/sms/send")
    public CommonResponse<String> sendSmsCode(@Valid @RequestBody SmsCodeRequest request) {
        authFacadeService.sendSmsCode(request);
        return CommonResponse.success(SuccessMessages.SMS_CODE_SENT);
    }

    @PostMapping("/sms/verify-client")
    public CommonResponse<TokenResponse> verifySmsCode(
            @Valid @RequestBody ClientLoginRequest request,
            HttpServletResponse response
    ) {
        TokenResponse tokenResponse = authFacadeService.verifySmsAndLoginClient(request, response);
        return CommonResponse.success(tokenResponse);
    }

    // ======== SUPPORT ========
    @PutMapping("/email/update-password")
    public CommonResponse<String> updatePassword(
            @Valid @RequestBody UpdatePasswordRequest request
    ) {
        authFacadeService.updatePassword(request);
        return CommonResponse.success(SuccessMessages.UPDATE_PASSWORD_SUCCESS);
    }

    @PostMapping("/email/login")
    public CommonResponse<TokenResponse> loginByEmail(
            @Valid @RequestBody SupportLoginRequest request,
            HttpServletResponse response
    ) {
        TokenResponse tokenResponse = authFacadeService.verifyAndLoginWithEmail(request, response);
        return CommonResponse.success(tokenResponse);
    }

    // ======== LOGOUT ========
    @PostMapping("/logout")
    public CommonResponse<String> logout(
            @CookieValue(name = COOKIE_NAME) String token,
            HttpServletResponse response
    ) {
        authFacadeService.logout(token, response);
        return CommonResponse.success(SuccessMessages.LOGOUT_SUCCESS);
    }

}
