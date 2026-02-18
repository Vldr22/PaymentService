package org.resume.paymentservice.service.facade;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.resume.paymentservice.model.dto.request.*;
import org.resume.paymentservice.model.dto.response.ClientResponse;
import org.resume.paymentservice.model.dto.response.EmployeeResponse;
import org.resume.paymentservice.model.dto.response.TokenResponse;
import org.resume.paymentservice.model.entity.User;
import org.resume.paymentservice.security.JwtBlacklistService;
import org.resume.paymentservice.security.JwtCookeService;
import org.resume.paymentservice.security.JwtService;
import org.resume.paymentservice.service.auth.AuthService;
import org.resume.paymentservice.service.auth.VerificationCodeService;
import org.resume.paymentservice.service.user.UserService;
import org.resume.paymentservice.utils.PhoneUtils;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthFacadeService {

    private final AuthService authService;
    private final UserService userService;
    private final JwtService jwtService;
    private final JwtCookeService jwtCookeService;
    private final JwtBlacklistService jwtBlacklistService;
    private final VerificationCodeService verificationCodeService;

    // ========== CLIENT ==============
    public ClientResponse registerClient(ClientRegistrationRequest request) {
        User user = authService.registerClient(request);
        log.info("Client registered: phone={}", user.getPhone());
        return toClientResponse(user);
    }

    public void sendSmsCode(SmsCodeRequest request) {
        String normalizedPhone = PhoneUtils.normalize(request.phone());
        userService.getUserByPhone(normalizedPhone);
        verificationCodeService.sendCode(normalizedPhone);
        log.info("SMS code sent: phone={}", normalizedPhone);
    }

    public TokenResponse verifySmsAndLoginClient(ClientLoginRequest request, HttpServletResponse response) {
        String normalizedPhone = PhoneUtils.normalize(request.phone());
        verificationCodeService.verifyCode(normalizedPhone, request.code());

        User user = userService.getUserByPhone(normalizedPhone);
        TokenResponse tokenResponse = generateTokenAndSetCookie(normalizedPhone, user, response);

        log.info("Client logged in: phone={}", normalizedPhone);
        return tokenResponse;
    }

    // ========== ADMIN ==============
    public EmployeeResponse createEmployee(EmployeeRegistrationRequest request) {
        User user = authService.createEmployee(request);
        log.info("Employee created with TempPassword: name={}, email={}, role={}",
                user.getName(), user.getEmail(), user.getRole());
        return toEmployeeResponse(user);
    }

    // ========== Employee ===========
    public void updatePassword(UpdatePasswordRequest request) {
        authService.validateEmployeeCredentials(request.email(), request.oldPassword());
        authService.updatePassword(request.email(), request.newPassword());
        log.info("Employee verify and change password success with email={}", request.email());
    }

    public TokenResponse verifyAndLoginWithEmail(SupportLoginRequest request, HttpServletResponse response) {
        authService.validateEmployeeCredentials(request.email(), request.password());

        User user = userService.getUserByEmail(request.email());
        TokenResponse tokenResponse = generateTokenAndSetCookie(request.email(), user, response);

        log.info("Employee logged in: email={}, role={}", request.email(), user.getRole());
        return tokenResponse;
    }

    // ========== LOGOUT ==============
    public void logout(String token, HttpServletResponse response) {
        String tokenId = jwtService.extractTokenId(token);
        long ttl = jwtService.calculateTtl(token);

        jwtBlacklistService.addToBlacklist(tokenId, ttl);
        jwtCookeService.clearAuthCookie(response);

        log.info("User logged out, token blacklisted: tokenId={}", tokenId);
    }

    // ========== HELPERS METHOD ==============
    private TokenResponse generateTokenAndSetCookie(String subject, User user, HttpServletResponse response) {
        String token = jwtService.generateToken(subject, user.getRole());
        jwtCookeService.setAuthCookie(response, token);
        return new TokenResponse(token);
    }

    private ClientResponse toClientResponse(User user) {
        return new ClientResponse(
                user.getName(),
                user.getSurname(),
                user.getPhone(),
                user.getUserStatus()
        );
    }

    private EmployeeResponse toEmployeeResponse(User user) {
        return new EmployeeResponse(
                user.getName(),
                user.getSurname(),
                user.getEmail(),
                user.getRole(),
                user.getUserStatus()
        );
    }

}
