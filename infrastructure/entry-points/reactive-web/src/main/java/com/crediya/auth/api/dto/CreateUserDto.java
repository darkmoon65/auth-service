package com.crediya.auth.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class CreateUserDto {

    @NotBlank(message = "El nombre es obligatorio")
    private String name;

    @NotBlank(message = "El apellido es obligatorio")
    private String lastname;

    private LocalDate birthday;

    private String address;

    @NotBlank(message = "El email es obligatorio")
    @Email(message = "El email no tiene un formato válido")
    private String email;
    private String password;
    private String phone;
    private String document;

    @Schema(description = "Id del rol asociado 1->admin, 2->applicant", example = "2")
    @Min(value = 1, message = "El idRol mínimo permitido es 1")
    @Max(value = 2, message = "El idRol máximo permitido es 2")
    private Integer idRol;

    @Schema(description = "Salario base del usuario", example = "5000.00")
    @NotNull(message = "El salario base es obligatorio")
    @DecimalMin(value = "0.0", inclusive = false, message = "El salario debe ser mayor a 0")
    private BigDecimal baseSalary;

}