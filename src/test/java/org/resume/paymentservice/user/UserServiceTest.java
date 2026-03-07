package org.resume.paymentservice.user;

import org.instancio.Instancio;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.resume.paymentservice.exception.AlreadyExistsException;
import org.resume.paymentservice.exception.NotFoundException;
import org.resume.paymentservice.model.entity.User;
import org.resume.paymentservice.repository.UserRepository;
import org.resume.paymentservice.service.user.UserService;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.instancio.Select.field;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("UserService — управление клиентами и Spring Security контекстом")
class UserServiceTest {

    private static final String PHONE = "+79001234567";
    private static final String STRIPE_CLIENT_ID = "cus_test_123";

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private User user;

    @BeforeEach
    void setUp() {
        user = Instancio.of(User.class)
                .set(field(User::getPhone), PHONE)
                .create();
    }

    @AfterEach
    void clearSecurityContext() {
        SecurityContextHolder.clearContext();
    }

    // getUserById

    /**
     * Возвращает пользователя по-существующему ID.
     */
    @Test
    void shouldReturnUser_whenIdExists() {
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

        User result = userService.getUserById(user.getId());

        assertThat(result).isEqualTo(user);
    }

    /**
     * Бросает NotFoundException если пользователь с таким ID не найден.
     */
    @Test
    void shouldThrowNotFound_whenUserIdNotExist() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.getUserById(1L))
                .isInstanceOf(NotFoundException.class);
    }

    // getUserByPhone

    /**
     * Возвращает пользователя по существующему номеру телефона.
     */
    @Test
    void shouldReturnUser_whenPhoneExists() {
        when(userRepository.findByPhone(PHONE)).thenReturn(Optional.of(user));

        User result = userService.getUserByPhone(PHONE);

        assertThat(result).isEqualTo(user);
    }

    /**
     * Бросает NotFoundException если пользователь с таким номером не найден.
     */
    @Test
    void shouldThrowNotFound_whenPhoneNotExist() {
        when(userRepository.findByPhone(PHONE)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.getUserByPhone(PHONE))
                .isInstanceOf(NotFoundException.class);
    }

    // createClient

    /**
     * Проверяет успешное создание клиента с ролью ROLE_USER.
     * Телефон уникален — дубликатов нет.
     */
    @Test
    void shouldCreateClient_whenPhoneNotTaken() {
        when(userRepository.existsByPhone(PHONE)).thenReturn(false);
        when(userRepository.save(any(User.class))).thenAnswer(inv -> inv.getArgument(0));

        User result = userService.createClient("Иван", "Иванов", "Иванович", PHONE);

        assertThat(result.getPhone()).isEqualTo(PHONE);
        assertThat(result.getName()).isEqualTo("Иван");
        verify(userRepository).save(any(User.class));
    }

    /**
     * Нельзя создать клиента с уже занятым номером телефона.
     */
    @Test
    void shouldThrowAlreadyExists_whenPhoneAlreadyTaken() {
        when(userRepository.existsByPhone(PHONE)).thenReturn(true);

        assertThatThrownBy(() -> userService.createClient("Иван", "Иванов", "Иванович", PHONE))
                .isInstanceOf(AlreadyExistsException.class);
    }

    // updateStripeCustomerId

    /**
     * Проверяет что Stripe Customer ID обновляется в БД
     * и устанавливается на объекте пользователя.
     */
    @Test
    void shouldUpdateStripeCustomerId_inDbAndOnObject() {
        userService.updateStripeCustomerId(user, STRIPE_CLIENT_ID);

        assertThat(user.getStripeCustomerId()).isEqualTo(STRIPE_CLIENT_ID);
        verify(userRepository).updateStripeCustomerId(user.getId(), STRIPE_CLIENT_ID);
    }

    // getCurrentUser

    /**
     * Проверяет что метод извлекает телефон из SecurityContext
     * и возвращает соответствующего пользователя из БД.
     */
    @Test
    void shouldReturnCurrentUser_fromSecurityContext() {
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(PHONE, null, List.of())
        );
        when(userRepository.findByPhone(PHONE)).thenReturn(Optional.of(user));

        User result = userService.getCurrentUser();

        assertThat(result).isEqualTo(user);
    }
}
