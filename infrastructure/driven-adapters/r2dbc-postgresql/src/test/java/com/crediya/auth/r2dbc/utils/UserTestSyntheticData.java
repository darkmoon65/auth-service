package com.crediya.auth.r2dbc.utils;

import com.crediya.auth.model.user.User;
import com.crediya.auth.r2dbc.entity.UserEntity;

import java.math.BigDecimal;
import java.time.LocalDate;

public class UserTestSyntheticData {
    public static UserEntity buildEntity() {
        return new UserEntity(
                "1",
                "Juan",
                "Pérez",
                LocalDate.of(1990, 5, 20),
                "Calle Falsa 123",
                "juan.perez@test.com",
                "987654321",
                "12345678",
                2,
                BigDecimal.valueOf(2500.00)
        );
    }

    public static User buildModel() {
        return User.builder()
                .id("1")
                .name("Juan")
                .lastname("Pérez")
                .birthday(LocalDate.of(1990, 5, 20))
                .address("Calle Falsa 123")
                .email("juan.perez@test.com")
                .phone("987654321")
                .document("12345678")
                .idRol(2)
                .baseSalary(BigDecimal.valueOf(2500.00))
                .build();
    }
}
