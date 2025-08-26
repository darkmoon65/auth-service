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

    public Mono<User> saveUser(User user) {
        // Si no hay rol definido asignar rol de solicitante
        if (user.getIdRol() == null) {
            user.setIdRol(2);
        }

        // validar rangos salary
        if (user.getBaseSalary().compareTo(BigDecimal.ZERO) < 0 ||
                user.getBaseSalary().compareTo(BigDecimal.valueOf(15000000)) > 0) {
            return Mono.error(new IllegalArgumentException("El salario debe estar entre 0 y 15000000"));
        }

        // validar existencia de email
        return userRepository.getUserByEmail(user.getEmail())
                .flatMap(existing ->
                        Mono.<User>error(new IllegalArgumentException("El correo ya está registrado")))
                .switchIfEmpty(userRepository.saveUser(user));
    }

    public Flux<User> getAllUsers() {
        return userRepository.getAllUsers();
    }

    public Mono<User> getUserByEmail(String email) {
        return userRepository.getUserByEmail(email);
    }
}

