/*
    Script for creating tables
    and inserting seed data (seeders)
*/

CREATE TABLE rol (
    rol_id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description VARCHAR(500)
);

CREATE TABLE users (
    user_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name VARCHAR(255) NOT NULL,
    lastname VARCHAR(255) NOT NULL,
    birthday DATE,
    address VARCHAR(255),
    email VARCHAR(255) NOT NULL,
    phone VARCHAR(50),
    document VARCHAR(50),
    rol_id SERIAL NOT NULL,
    base_salary NUMERIC(15,2) NOT NULL,
    CONSTRAINT fk_user_rol FOREIGN KEY (rol_id) REFERENCES rol(rol_id)
);

INSERT INTO rol (rol_id, name, description)
VALUES
    (1, 'ADMIN', 'System administrator'),
    (2, 'APPLICANT', 'Applicant user');