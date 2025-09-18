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
    password VARCHAR(1024) NOT NULL,
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

INSERT INTO users (name, lastname, birthday, address, email, password, phone, document, rol_id, base_salary)
VALUES (
    'Admin name',
    'Admin lastname',
    '1994-01-01',
    'Calle las manzanas 123',
    'admin@system.com',
    '$2y$10$DpqdilkhvZHT7BFKLIOt.OCFz8XBVCXbrChr5m75T2xrMmcDPEkea',
    '99123123',
    '77123123',
    1,
    7000.00
);