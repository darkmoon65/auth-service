package com.crediya.auth.usecase.user;

import com.crediya.auth.model.user.User;
import com.crediya.auth.model.user.gateways.UserRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class UserUseCase {
    private final UserRepository userRepository;

    public Mono<User> saveUser(User user) {
        return userRepository.saveUser(user);
    }

    public Flux<User> getAllUsers() {
        return userRepository.getAllUsers();
    }

    public Mono<User> getUserById(String id) {
        return userRepository.getUserById(id);
    }
}

