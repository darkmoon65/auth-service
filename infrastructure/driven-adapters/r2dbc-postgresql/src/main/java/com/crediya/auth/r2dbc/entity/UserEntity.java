package com.crediya.auth.r2dbc.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;
import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table("users")
public class UserEntity {
    @Id
    @Column("user_id")
    private String id;
    @NonNull
    private String name;
    @NonNull
    private String lastname;
    private LocalDate birthday;
    private String address;
    @NonNull
    private String email;
    private String phone;
    private String document;
    @Column("rol_id")
    private Integer idRol;
    @NonNull
    @Column("base_salary")
    private BigDecimal baseSalary;
}
