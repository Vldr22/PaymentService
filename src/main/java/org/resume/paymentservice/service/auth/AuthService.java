package org.resume.paymentservice.service.auth;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.resume.paymentservice.exception.AlreadyExistsException;
import org.resume.paymentservice.exception.AuthException;
import org.resume.paymentservice.model.dto.request.ClientRegistrationRequest;
import org.resume.paymentservice.model.dto.request.EmployeeRegistrationRequest;
import org.resume.paymentservice.model.entity.User;
import org.resume.paymentservice.model.enums.Roles;
import org.resume.paymentservice.service.user.UserService;
import org.resume.paymentservice.utils.CodeGenerator;
import org.resume.paymentservice.utils.ErrorMessages;
import org.resume.paymentservice.utils.PhoneUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    public User registerClient(ClientRegistrationRequest request) {
        String normalizedPhone = PhoneUtils.normalize(request.phone());

        if (userService.existsByPhone(normalizedPhone)) {
            throw AlreadyExistsException.userByPhone(request.phone());
        }

        return userService.createClient(request.name(), request.surname(), request.midname(), normalizedPhone);
    }

    public User createEmployee(EmployeeRegistrationRequest request) {
        if (userService.existsByEmail(request.email())) {
            throw AlreadyExistsException.employeeByEmail(request.email());
        }

        String tempPassword = CodeGenerator.generatePassword();
        String encodedPassword = passwordEncoder.encode(tempPassword);

        log.info("TempPassword: {}", tempPassword);

        return userService.createEmployee(
                request.name(),
                request.surname(),
                request.midname(),
                request.email(),
                encodedPassword,
                Roles.ROLE_EMPLOYEE
        );
    }

    public void updatePassword(String email, String newPassword) {
        String encodedPassword = passwordEncoder.encode(newPassword);
        userService.updateEmployeePassword(email, encodedPassword);
    }

    public void validateEmployeeCredentials(String email, String enteredPassword) {
        User user = userService.getUserByEmail(email);

        if (!passwordEncoder.matches(enteredPassword, user.getPassword())) {
            throw AuthException.invalidCredentials();
        }
    }
}
