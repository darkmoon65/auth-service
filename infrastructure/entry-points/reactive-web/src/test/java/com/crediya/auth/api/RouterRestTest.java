package com.crediya.auth.api;

import com.crediya.auth.api.config.UserPath;
import com.crediya.auth.api.dto.CreateUserDto;
import com.crediya.auth.api.mapper.UserDtoMapper;
import com.crediya.auth.api.validation.UserValidator;
import com.crediya.auth.model.user.User;
import com.crediya.auth.usecase.user.UserUseCase;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@WebFluxTest(RouterRest.class)
@ContextConfiguration(classes = {RouterRest.class, Handler.class, RouterRestTest.TestConfig.class})
class RouterRestTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockitoBean
    private UserUseCase userUseCase;

    @MockitoBean
    private UserDtoMapper userDtoMapper;

    @MockitoBean
    private UserValidator userValidator;

    @TestConfiguration
    static class TestConfig {
        @Bean
        UserPath userPath() {
            UserPath path = new UserPath();
            path.setUsers("/api/v1/users");
            path.setUsersById("/api/v1/users/{email}");
            return path;
        }
    }

    @Test
    void testListenPOSTUseCase() {
        when(userUseCase.saveUser(any())).thenReturn(Mono.just(new User()));
        when(userValidator.validate(any())).thenReturn(Mono.just(new CreateUserDto()));
        when(userDtoMapper.toResponse(any())).thenReturn(new User());

        Map<String, Object> user = Map.of(
                "name", "cesar",
                "lastname", "santillana",
                "birthday", "2000-10-15",
                "address", "Calle las gaviotas 123",
                "phone", "991123772",
                "document", "77123123",
                "email", "cs60@gmail.com",
                "baseSalary", 5000
        );

        webTestClient.post()
                .uri("/api/v1/users")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(user)
                .exchange()
                .expectStatus().isCreated();
    }

    @Test
    void testListenPOSTFailUseCase() {
        when(userUseCase.saveUser(any())).thenReturn(Mono.empty());
        when(userDtoMapper.toResponse(any())).thenReturn(new User());

        Map<String, Object> user = Map.of(
                "name", "cesar",
                "lastname", "santillana",
                "birthday", "2000-10-15",
                "address", "Calle las gaviotas 123",
                "phone", "991123772",
                "document", "77123123",
                "email", "cs6@gmail.com"
        );

        webTestClient.post()
                .uri("/api/v1/users")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(user)
                .exchange()
                .expectStatus().is5xxServerError();
    }
}