package com.crediya.auth.model.user;
import lombok.Builder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class User {
    private String id;
    private String name;
    private String lastname;
    private Date birthday;
    private String address;
    private String email;
    private String phone;
    private String document;
    private String idRol;
    private BigDecimal baseSalary;
}
