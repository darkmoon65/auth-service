package com.crediya.auth.model.user.gateways;

import com.crediya.auth.model.user.User;
import com.crediya.auth.model.user.dto.LogInDTO;
import com.crediya.auth.model.user.dto.TokenDTO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface UserRepository {

    Mono<User> saveUser(User user);

    Flux<User> getAllUsers();

    Mono<User> getUserByEmail(String email);

    Mono<TokenDTO> login(LogInDTO dto);
}
