package com.crediya.auth.api.validation;

import com.crediya.auth.api.dto.CreateUserDto;
import com.crediya.auth.api.exception.UserValidationException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class UserValidator {
    private final Validator validator;

    public Mono<CreateUserDto> validate(CreateUserDto user) {
        Set<ConstraintViolation<CreateUserDto>> violations = validator.validate(user);

        if (!violations.isEmpty()) {
            Map<String, String> errors = violations.stream().collect(
                    Collectors.toMap(
                            v -> v.getPropertyPath().toString(),
                            ConstraintViolation::getMessage,
                            (msg1, msg2) -> msg1 + ", " + msg2
                    ));

            return Mono.error(new UserValidationException("Errores de validación", errors));
        }

        return Mono.just(user);
    }
}
