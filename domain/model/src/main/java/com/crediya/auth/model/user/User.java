package com.crediya.auth.model.user;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class User {
    private String id;
    private String name;
    private String lastname;
    private LocalDate birthday;
    private String address;
    private String email;
    private String phone;
    private String document;
    private Integer idRol;
    private BigDecimal baseSalary;
}
