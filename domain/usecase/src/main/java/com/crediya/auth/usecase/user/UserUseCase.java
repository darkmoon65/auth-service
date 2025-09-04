package com.crediya.auth.usecase.user;

import com.crediya.auth.model.user.User;
import com.crediya.auth.model.user.gateways.UserRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

@RequiredArgsConstructor
public class UserUseCase {
    private final UserRepository userRepository;
    private final BigDecimal limiteInferior = BigDecimal.ZERO;
    private final BigDecimal limiteSuperior = BigDecimal.valueOf(15000000);

    public Mono<User> saveUser(User user) {

        return Mono.just(user)
                .map(u -> {
                    if (u.getIdRol() == null) {
                        u.setIdRol(2);
                    }
                    return u;
                })
                .filter(u -> u.getBaseSalary().compareTo(limiteInferior) >= 0
                        && u.getBaseSalary().compareTo(limiteSuperior) <= 0)
                .switchIfEmpty(Mono.error(
                        new IllegalArgumentException("El salario debe estar entre " + limiteInferior + " y " + limiteSuperior)
                ))
                .flatMap(u -> userRepository.getUserByEmail(u.getEmail())
                        .flatMap(exist -> Mono.<User>error(new IllegalArgumentException("El correo ya está registrado")))
                        .switchIfEmpty(userRepository.saveUser(u))
                );
    }

    public Flux<User> getAllUsers() {
        return userRepository.getAllUsers();
    }

    public Mono<User> getUserByEmail(String email) {
        return userRepository.getUserByEmail(email);
    }
}

