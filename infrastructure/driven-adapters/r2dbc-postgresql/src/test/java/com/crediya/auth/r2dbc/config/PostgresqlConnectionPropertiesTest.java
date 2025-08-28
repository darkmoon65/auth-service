package com.crediya.auth.r2dbc.config;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PostgresqlConnectionPropertiesTest {

    @Test
    void shouldCreatePropertiesWithGivenValues() {
        PostgresqlConnectionProperties props = new PostgresqlConnectionProperties(
                "localhost",
                5432,
                "postgres",
                "crediya-db",
                "postgres",
                "password"
        );

        assertEquals("localhost", props.host());
        assertEquals(5432, props.port());
        assertEquals("postgres", props.database());
        assertEquals("crediya-db", props.schema());
        assertEquals("postgres", props.username());
        assertEquals("password", props.password());
    }
}