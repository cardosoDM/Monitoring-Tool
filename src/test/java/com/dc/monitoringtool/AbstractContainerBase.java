package com.dc.monitoringtool;

import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.containers.PostgreSQLContainer;

public abstract class AbstractContainerBase {

    protected static final PostgreSQLContainer<?> POSTGRES_CONTAINER;
    protected static final MongoDBContainer MONGO_DB_CONTAINER;


    static {
        POSTGRES_CONTAINER = new PostgreSQLContainer<>("postgres:latest")
                .withUsername("username")
                .withPassword("password")
                .withDatabaseName("monitoringtool");
        POSTGRES_CONTAINER.start();

        MONGO_DB_CONTAINER = new MongoDBContainer("mongo:latest");
        MONGO_DB_CONTAINER.start();
    }

    @DynamicPropertySource
    public static void dynamicPropertySource(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", POSTGRES_CONTAINER::getJdbcUrl);
        registry.add("spring.datasource.username", POSTGRES_CONTAINER::getUsername);
        registry.add("spring.datasource.password", POSTGRES_CONTAINER::getPassword);
        registry.add("spring.quartz.properties.org.quartz.jobStore.driverDelegateClass", () -> "org.quartz.impl.jdbcjobstore.PostgreSQLDelegate");

    }
}