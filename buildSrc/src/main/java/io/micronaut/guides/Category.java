package io.micronaut.guides;

import io.micronaut.core.order.Ordered;

public enum Category implements Ordered {

    // GETTING STARTED
    GETTING_STARTED("Getting Started", 1),
    CORE_BASICS("Core Basics", 2),
    VALIDATION("Validation", 3),
    DEVELOPMENT("Development", 4),
    TEST("Testing", 5),

    // AI
    MCP("MCP", 6),

    // BEYOND THE BASICS
    OBJECT_STORAGE("Object Storage", 7),
    EMAIL("Email", 8),
    MESSAGING("Messaging", 9),
    LOGGING("Logging", 10),
    SCHEDULING("Scheduling", 11),
    CACHE("Cache", 12),
    PATTERNS("Patterns", 13),
    INTERNATIONALIZATION("i18n", 14),

    // DATA ACCESS
    DATABASE_MODELING("Database Modeling", 15),
    DATA_JDBC("Data JDBC", 16),
    DATA_JPA("Data JPA", 17),
    SCHEMA_MIGRATION("Schema Migration", 18),
    DATA_RDBC("Data R2DBC", 19),
    DATA_MONGO("MongoDB", 20),
    DATA_ACCESS("Data Access", 21),

    // SECURITY
    SECURITY("Micronaut Security", 22),
    AUTHORIZATION_CODE("Authorization Code", 23),
    CLIENT_CREDENTIALS("Client Credentials", 24),
    SECRETS_MANAGER("Secrets Manager", 25),

    // HTTP & API
    HTTP("HTTP Server", 26),
    HTTP_CLIENT("HTTP Client", 27),
    BEYOND_JSON("Beyond JSON", 28),
    JAX_RS("JAX-RS", 29),
    WEBSOCKETS("WebSockets", 30),
    GRAPHQL("GraphQL", 31),
    OPEN_API("OpenAPI", 32),
    JSON_SCHEMA("JSON Schema", 33),

    // DISTRIBUTED SYSTEMS
    DISTRIBUTED_TRACING("Distributed Tracing", 34),
    SERVICE_DISCOVERY("Service Discovery", 35),
    DISTRIBUTED_CONFIGURATION("Distributed Configuration", 36),
    METRICS("Metrics", 37),

    // DISTRIBUTION
    DISTRIBUTION("Distribution", 38),
    REGISTRY("Registry", 39),
    GRAALVM("GraalVM", 40),
    CRAC("Coordinated Restore at Checkpoint", 41),
    KUBERNETES("Kubernetes", 42),

    // CLOUD
    SERVERLESS("Serverless", 43),
    AWS_LAMBDA("AWS Lambda", 44),
    SCALE_TO_ZERO_CONTAINERS("Scale to Zero Containers", 45),

    // SERVER-SIDE HTML
    VIEWS("Views", 46),
    TURBO("Turbo", 47),
    STATIC_RESOURCES("Static Resources", 48),

    // LANGUAGES
    KOTLIN("Kotlin", 49),
    GRAALPY("GraalPy", 50),

    // FRAMEWORKS
    SPRING("Spring Boot", 51),
    SPRING_BOOT_TO_MICRONAUT_BUILDING_A_REST_API("Boot to Micronaut Building a REST API", 52);

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