package com.crediya.auth.api;

import com.crediya.auth.api.exception.GlobalExceptionHandler;
import com.crediya.auth.api.exception.UserValidationException;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.HashMap;
import java.util.Map;

@WebFluxTest
@ContextConfiguration(classes = {GlobalExceptionHandler.class})
class GlobalExceptionHandlerTest {

    @RestController
    static class TestController {
        @GetMapping("/illegal")
        public String throwException() {
            throw new IllegalArgumentException("Test exception");
        }

        @GetMapping("/invalidParameters")
        public String throwException2() {
            Map<String, String> errors = new HashMap<>();
            errors.put("name", "El nombre es obligatorio");
            errors.put("email", "El email no es válido");

            throw new UserValidationException("Validacion error", errors);
        }
    }

    private final WebTestClient webTestClient = WebTestClient.bindToController(new TestController())
            .controllerAdvice(new GlobalExceptionHandler())
            .build();

    @Test
    void handleIllegalArgument_shouldReturnBadRequest() {
        webTestClient.get()
                .uri("/illegal")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().is4xxClientError()
                .expectBody()
                .jsonPath("$.message").isEqualTo("Test exception")
                .jsonPath("$.datetime").exists();
    }

    @Test
    void handleUserValidationException_shouldReturnBadRequestWithErrors() {
        webTestClient.get()
                .uri("/invalidParameters")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath("$.errors.name").isEqualTo("El nombre es obligatorio")
                .jsonPath("$.errors.email").isEqualTo("El email no es válido")
                .jsonPath("$.datetime").exists();
    }
}
