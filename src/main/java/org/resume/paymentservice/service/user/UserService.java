package org.resume.paymentservice.service.user;

import lombok.RequiredArgsConstructor;
import org.resume.paymentservice.exception.NotFoundException;
import org.resume.paymentservice.model.entity.User;
import org.resume.paymentservice.model.enums.Roles;
import org.resume.paymentservice.repository.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public User getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> NotFoundException.userById(userId));
    }

    public User getUserByPhone(String phone) {
        return userRepository.findByPhone(phone)
                .orElseThrow(() -> NotFoundException.userByPhone(phone));
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> NotFoundException.userByEmail(email));
    }

    public boolean existsByPhone(String phone) {
        return userRepository.existsByPhone(phone);
    }

    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    public User createClient(String name, String surname, String midname, String phone) {
        User user = new User(name, surname, midname, phone);
        return userRepository.save(user);
    }

    public User createEmployee(String name, String surname, String midname,
                               String email, String encodedPassword, Roles role) {
        User user = new User(name, surname, midname, email, encodedPassword, role);
        return userRepository.save(user);
    }

    public void updateEmployeePassword(String email, String encodedPassword) {
        User user = getUserByEmail(email);
        user.setPassword(encodedPassword);
        userRepository.save(user);
    }

    public User getCurrentUser() {
        String subject = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();

        return userRepository.findByPhone(subject)
                .or(() -> userRepository.findByEmail(subject))
                .orElseThrow(() -> NotFoundException.userByPhone(subject));
    }
}
