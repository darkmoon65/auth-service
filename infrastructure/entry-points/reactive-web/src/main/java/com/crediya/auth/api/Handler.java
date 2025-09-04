package com.crediya.auth.api;

import com.crediya.auth.api.dto.CreateUserDto;
import com.crediya.auth.api.mapper.UserDtoMapper;
import com.crediya.auth.api.validation.UserValidator;
import com.crediya.auth.usecase.user.UserUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import jakarta.validation.constraints.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class Handler {
    private final UserUseCase userUseCase;
    private final UserDtoMapper userMapper;
    private final UserValidator userValidator;

    public Mono<ServerResponse> listenCreateUser(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(CreateUserDto.class)
                .doOnNext(user -> log.info("Received user: {}", user))
                .flatMap(userValidator::validate)
                .map(userMapper::toResponse)
                .flatMap(userUseCase::saveUser)
                .flatMap(savedUser -> ServerResponse.status(201)
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(savedUser));
    }


}
