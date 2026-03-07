package org.resume.paymentservice.service.user;

import lombok.RequiredArgsConstructor;
import org.resume.paymentservice.exception.AlreadyExistsException;
import org.resume.paymentservice.exception.NotFoundException;
import org.resume.paymentservice.model.entity.User;
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

    public User createClient(String name, String surname, String midname, String phone) {
        if (userRepository.existsByPhone(phone)) {
            throw AlreadyExistsException.userByPhone(phone);
        }
        User user = new User(name, surname, midname, phone);
        return userRepository.save(user);
    }

    public void updateStripeCustomerId(User user, String stripeCustomerId) {
        userRepository.updateStripeCustomerId(user.getId(), stripeCustomerId);
        user.setStripeCustomerId(stripeCustomerId);
    }

    public User getCurrentUser() {
        String phone = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();
        return getUserByPhone(phone);
    }
}
