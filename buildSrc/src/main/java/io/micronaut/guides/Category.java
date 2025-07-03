package io.micronaut.guides;

import io.micronaut.core.order.Ordered;

public enum Category implements Ordered {

    // GETTING STARTED
    GETTING_STARTED("Getting Started", 1),
    CORE_BASICS("Core Basics", 2),
    VALIDATION("Validation", 3),
    DEVELOPMENT("Development", 4),
    TEST("Testing", 5),

    // BEYOND THE BASICS
    OBJECT_STORAGE("Object Storage", 6),
    EMAIL("Email", 7),
    MESSAGING("Messaging", 8),
    LOGGING("Logging", 9),
    SCHEDULING("Scheduling", 10),
    CACHE("Cache", 11),
    PATTERNS("Patterns", 12),
    INTERNATIONALIZATION("i18n", 13),

    // DATA ACCESS
    DATABASE_MODELING("Database Modeling", 14),
    DATA_JDBC("Data JDBC", 15),
    DATA_JPA("Data JPA", 16),
    SCHEMA_MIGRATION("Schema Migration", 17),
    DATA_RDBC("Data R2DBC", 18),
    DATA_MONGO("MongoDB", 19),
    DATA_ACCESS("Data Access", 20),

    // SECURITY
    SECURITY("Micronaut Security", 21),
    AUTHORIZATION_CODE("Authorization Code", 22),
    CLIENT_CREDENTIALS("Client Credentials", 23),
    SECRETS_MANAGER("Secrets Manager", 24),

    // HTTP & API
    HTTP("HTTP Server", 25),
    HTTP_CLIENT("HTTP Client", 26),
    BEYOND_JSON("Beyond JSON", 27),
    JAX_RS("JAX-RS", 28),
    WEBSOCKETS("WebSockets", 29),
    GRAPHQL("GraphQL", 30),
    OPEN_API("OpenAPI", 31),
    JSON_SCHEMA("JSON Schema", 32),

    // DISTRIBUTED SYSTEMS
    DISTRIBUTED_TRACING("Distributed Tracing", 33),
    SERVICE_DISCOVERY("Service Discovery", 34),
    DISTRIBUTED_CONFIGURATION("Distributed Configuration", 35),
    METRICS("Metrics", 36),

    // DISTRIBUTION
    DISTRIBUTION("Distribution", 37),
    REGISTRY("Registry", 38),
    GRAALVM("GraalVM", 39),
    CRAC("Coordinated Restore at Checkpoint", 40),
    KUBERNETES("Kubernetes", 41),

    // CLOUD
    SERVERLESS("Serverless", 42),
    AWS_LAMBDA("AWS Lambda", 43),
    SCALE_TO_ZERO_CONTAINERS("Scale to Zero Containers", 44),

    // SERVER-SIDE HTML
    VIEWS("Views", 45),
    TURBO("Turbo", 46),
    STATIC_RESOURCES("Static Resources", 47),

    // LANGUAGES
    KOTLIN("Kotlin", 48),
    GRAALPY("GraalPy", 49),

    // FRAMEWORKS
    SPRING("Spring Boot", 50),
    SPRING_BOOT_TO_MICRONAUT_BUILDING_A_REST_API("Boot to Micronaut Building a REST API", 51);

    private final String val;
    private final int order;

    Category(String val, int order) {
        this.val = val;
        this.order = order;
    }

    @Override
    public String toString() {
        return val;
    }

    @Override
    public int getOrder() {
        return order;
    }
}