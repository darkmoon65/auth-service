package com.crediya.auth.r2dbc.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Getter
@Setter
@Table("rol")
public class RoleEntity {
    @Id
    @Column("rol_id")
    private Integer id;
    private String name;
    private String description;
}
