/*
    Script for creating tables
    and inserting seed data (seeders)
*/

CREATE TABLE rol (
    id_rol UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name VARCHAR(255) NOT NULL,
    description VARCHAR(500)
);

CREATE TABLE users (
    id_user UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name VARCHAR(255) NOT NULL,
    lastname VARCHAR(255) NOT NULL,
    birthday DATE,
    address VARCHAR(255),
    email VARCHAR(255) NOT NULL,
    phone VARCHAR(50),
    document VARCHAR(50),
    id_rol UUID,
    base_salary NUMERIC(15,2) NOT NULL
    CONSTRAINT fk_user_rol FOREIGN KEY (id_rol) REFERENCES rol(id_rol)
);

INSERT INTO rol (id_rol, nombre, descripcion)
VALUES
    (gen_random_uuid(), 'ADMIN', 'System administrator'),
    (gen_random_uuid(), 'APPLICANT', 'Applicant user');