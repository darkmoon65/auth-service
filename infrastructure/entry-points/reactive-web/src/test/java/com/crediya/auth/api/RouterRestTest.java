package com.crediya.auth.api;

import com.crediya.auth.api.config.UserPath;
import com.crediya.auth.api.mapper.UserDtoMapper;
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
    void testListenGETUseCase() {
        when(userUseCase.getAllUsers()).thenReturn(Flux.empty());
        
        webTestClient.get()
                .uri("/api/v1/users")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody().json("[]");
    }


    @Test
    void testListenGETUserByEmail() {
        when(userUseCase.getUserByEmail("testnotfound@gmail.com")).thenReturn(Mono.empty());

        webTestClient.get()
                .uri("/api/v1/users/testnotfound@gmail.com")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().is4xxClientError();
    }

    @Test
    void testListenPOSTUseCase() {
        when(userUseCase.saveUser(any())).thenReturn(Mono.empty());
        when(userDtoMapper.toResponse(any())).thenReturn(new User());

        Map<String, Object> user = Map.of(
                "name", "cesar",
                "lastname", "santillana",
                "birthday", "2000-10-15",
                "address", "Calle las gaviotas 123",
                "phone", "991123772",
                "document", "77123123",
                "email", "cs6@gmail.com",
                "baseSalary", 5000
        );

        webTestClient.post()
                .uri("/api/v1/users")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(user)
                .exchange()
                .expectStatus().isOk();
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