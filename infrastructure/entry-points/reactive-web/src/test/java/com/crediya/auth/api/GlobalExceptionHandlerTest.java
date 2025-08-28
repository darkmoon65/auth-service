package com.crediya.auth.api;

import com.crediya.auth.api.exception.GlobalExceptionHandler;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.test.web.reactive.server.WebTestClient;

@WebFluxTest
@ContextConfiguration(classes = {GlobalExceptionHandler.class})
class GlobalExceptionHandlerTest {

    @RestController
    static class TestController {
        @GetMapping("/illegal")
        public String throwException() {
            throw new IllegalArgumentException("Test exception");
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
                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath("$.status").isEqualTo(400)
                .jsonPath("$.error").isEqualTo("Bad Request")
                .jsonPath("$.message").isEqualTo("Test exception")
                .jsonPath("$.datetime").exists();
    }
}
