package com.crediya.auth.usecase.user;

import com.crediya.auth.model.user.User;
import com.crediya.auth.model.user.gateways.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserUseCaseTest {

    @InjectMocks
    UserUseCase userUseCase;

    @Mock
    UserRepository userRepository;

    public User testUser;
    public User testUser2;

    @BeforeEach
    void setup() {
        testUser = new User();
        testUser.setEmail("cs@gmail.com");
        testUser.setBaseSalary(BigDecimal.valueOf(6000));

        testUser2 = new User();
        testUser2.setEmail("cs2@gmail.com");
        testUser2.setBaseSalary(BigDecimal.valueOf(4000));
    }

    @Test
    void saveUser_ok() {
        String email = "cs@gmail.com";

        when(userRepository.getUserByEmail(anyString())).thenReturn(Mono.empty());
        when(userRepository.saveUser(any(User.class)))
                .thenAnswer(inv -> Mono.just(inv.getArgument(0)));

        StepVerifier.create(userUseCase.saveUser(testUser))
                .assertNext(saved -> assertThat(saved.getIdRol()).isEqualTo(2))
                .verifyComplete();

        verify(userRepository).getUserByEmail(email);
        verify(userRepository).saveUser(any(User.class));
    }

    @Test
    void saveUser_shouldFailWhenSalaryNegative() {
        testUser.setBaseSalary(BigDecimal.valueOf(-100));

        StepVerifier.create(userUseCase.saveUser(testUser))
                .expectErrorMatches(ex -> ex instanceof IllegalArgumentException &&
                        ex.getMessage().equals("El salario debe estar entre 0 y 15000000"))
                .verify();
    }

    @Test
    void saveUser_shouldPassWhenSalaryZero() {
        testUser.setBaseSalary(BigDecimal.ZERO);

        when(userRepository.getUserByEmail(anyString())).thenReturn(Mono.empty());
        when(userRepository.saveUser(any(User.class))).thenReturn(Mono.just(testUser));

        StepVerifier.create(userUseCase.saveUser(testUser))
                .assertNext(saved -> assertThat(saved.getBaseSalary()).isEqualTo(BigDecimal.ZERO))
                .verifyComplete();
    }

    @Test
    void saveUser_shouldFailWhenSalaryTop() {
        testUser.setBaseSalary(BigDecimal.valueOf(15000001));

        StepVerifier.create(userUseCase.saveUser(testUser))
                .expectErrorMatches(ex -> ex instanceof IllegalArgumentException &&
                        ex.getMessage().equals("El salario debe estar entre 0 y 15000000"))
                .verify();
    }

    @Test
    void saveUser_withRole() {
        testUser.setIdRol(1);

        when(userRepository.getUserByEmail(anyString())).thenReturn(Mono.empty());
        when(userRepository.saveUser(any(User.class))).thenReturn(Mono.just(testUser));

        StepVerifier.create(userUseCase.saveUser(testUser))
                .assertNext(saved -> assertThat(saved.getIdRol()).isEqualTo(1))
                .verifyComplete();

        verify(userRepository).saveUser(any(User.class));
    }

    @Test
    void saveUser_shouldFailWhenEmailExist() {
        String email = "cs@gmail.com";

        User existingUser = new User();
        existingUser.setEmail(email);

        when(userRepository.getUserByEmail(email)).thenReturn(Mono.just(existingUser));
        when(userRepository.saveUser(any(User.class))).thenReturn(Mono.just(testUser));

        StepVerifier.create(userUseCase.saveUser(testUser))
                .expectErrorMatches(throwable ->
                        throwable instanceof IllegalArgumentException &&
                                throwable.getMessage().equals("El correo ya está registrado"))
                .verify();

        verify(userRepository).getUserByEmail(email);
    }

    @Test
    void getAllUsers() {
        List<User> users = List.of(testUser, testUser2);

        when(userRepository.getAllUsers()).thenReturn(Flux.fromIterable(users));

        StepVerifier.create(userUseCase.getAllUsers())
                .expectNextSequence(users)
                .verifyComplete();

        verify(userRepository).getAllUsers();
    }

    @Test
    void getUserByEmail() {
        String email = "cs@gmail.com";
        when(userRepository.getUserByEmail(email)).thenReturn(Mono.just(testUser));

        StepVerifier.create(userUseCase.getUserByEmail(email))
                .assertNext(user -> assertThat(user.getEmail()).isEqualTo(email))
                .verifyComplete();

        verify(userRepository).getUserByEmail(email);
    }
}
