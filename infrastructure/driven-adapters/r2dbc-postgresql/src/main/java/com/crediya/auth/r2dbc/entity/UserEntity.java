package com.crediya.auth.r2dbc.entity;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Table("users")
public class UserEntity {
    @Id
    private String id;
    @NonNull
    private String name;
    @NonNull
    private String lastname;
    private Date birthday;
    private String address;
    @NonNull
    private String email;
    private String phone;
    private String document;
    private String idRol;
    @NonNull
    @Column("base_salary")
    private BigDecimal baseSalary;
}
