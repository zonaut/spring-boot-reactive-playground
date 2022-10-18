package com.zonaut.playground.reactive;

import org.springframework.boot.test.autoconfigure.data.r2dbc.DataR2dbcTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import static java.lang.String.format;

@Testcontainers
@DataR2dbcTest
public class BaseRepositoryIntegrationTest {

    static PostgreSQLContainer<?> postgreSQL = new PostgreSQLContainer<>(DockerImageName.parse("postgres:latest"));

    static {
        postgreSQL.start();
    }

    @DynamicPropertySource
    static void registerPostgreSQLProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.r2dbc.url", () -> format("r2dbc:postgresql://%s:%s/%s",
                postgreSQL.getHost(),
                postgreSQL.getMappedPort(PostgreSQLContainer.POSTGRESQL_PORT),
                postgreSQL.getDatabaseName()));

        registry.add("spring.r2dbc.username", postgreSQL::getUsername);
        registry.add("spring.r2dbc.password", postgreSQL::getUsername);

        registry.add("spring.liquibase.url", postgreSQL::getJdbcUrl);
        registry.add("spring.liquibase.user", postgreSQL::getUsername);
        registry.add("spring.liquibase.password", postgreSQL::getUsername);
    }
}
