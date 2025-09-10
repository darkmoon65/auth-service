package com.crediya.auth.security.dto;

public record SignUpDTO(String name,
                        String lastName,
                        String email,
                        String password) {
}