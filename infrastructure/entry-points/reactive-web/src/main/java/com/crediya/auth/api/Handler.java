package com.crediya.auth.api;

import com.crediya.auth.api.dto.CreateUserDto;
import com.crediya.auth.model.user.dto.LogInDTO;
import com.crediya.auth.model.user.dto.TokenDTO;
import com.crediya.auth.api.mapper.UserDtoMapper;
import com.crediya.auth.api.validation.UserValidator;
import com.crediya.auth.security.exception.LoginValidationException;
import com.crediya.auth.usecase.user.UserUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Slf4j
@Component
@RequiredArgsConstructor
public class Handler {
    private final UserUseCase userUseCase;
    private final UserDtoMapper userMapper;
    private final UserValidator userValidator;

    @PreAuthorize("hasAuthority('ADMIN')")
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

    public Mono<ServerResponse> logIn(ServerRequest request) {
        return request.bodyToMono(LogInDTO.class)
                .switchIfEmpty(Mono.error(new IllegalArgumentException("El body no puede estar vacío")))
                .doOnNext(logDto -> log.info("LOGGGG {}", logDto))
                .flatMap(dto -> userUseCase.login(dto)
                        .flatMap(
                                token -> ServerResponse.ok()
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .bodyValue(token))
                );
    }

    public Mono<ServerResponse> getUserByEmail(ServerRequest request) {
        String email = request.pathVariable("email");

        return userUseCase.getUserByEmail(email)
                .flatMap(user -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(user))
                .switchIfEmpty(ServerResponse.notFound().build());
    }
}
