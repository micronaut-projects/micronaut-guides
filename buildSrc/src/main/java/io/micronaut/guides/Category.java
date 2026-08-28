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
    LANGCHAIN4J("LANGCHAIN4J", 7),

    // BEYOND THE BASICS
    OBJECT_STORAGE("Object Storage", 8),
    EMAIL("Email", 9),
    MESSAGING("Messaging", 10),
    LOGGING("Logging", 11),
    SCHEDULING("Scheduling", 12),
    CACHE("Cache", 13),
    PATTERNS("Patterns", 14),
    INTERNATIONALIZATION("i18n", 15),

    // DATA ACCESS
    DATABASE_MODELING("Database Modeling", 16),
    DATA_JDBC("Data JDBC", 17),
    DATA_JPA("Data JPA", 18),
    SCHEMA_MIGRATION("Schema Migration", 19),
    DATA_RDBC("Data R2DBC", 20),
    DATA_MONGO("MongoDB", 21),
    DATA_ACCESS("Data Access", 22),

    // SECURITY
    SECURITY("Micronaut Security", 23),
    AUTHORIZATION_CODE("Authorization Code", 24),
    CLIENT_CREDENTIALS("Client Credentials", 25),
    SECRETS_MANAGER("Secrets Manager", 26),

    // HTTP & API
    HTTP("HTTP Server", 27),
    HTTP_CLIENT("HTTP Client", 28),
    BEYOND_JSON("Beyond JSON", 29),
    JAX_RS("JAX-RS", 30),
    WEBSOCKETS("WebSockets", 31),
    GRAPHQL("GraphQL", 32),
    OPEN_API("OpenAPI", 33),
    JSON_SCHEMA("JSON Schema", 34),

    // DISTRIBUTED SYSTEMS
    DISTRIBUTED_TRACING("Distributed Tracing", 35),
    SERVICE_DISCOVERY("Service Discovery", 36),
    DISTRIBUTED_CONFIGURATION("Distributed Configuration", 37),
    METRICS("Metrics", 38),

    // DISTRIBUTION
    DISTRIBUTION("Distribution", 39),
    REGISTRY("Registry", 40),
    GRAALVM("GraalVM", 41),
    CRAC("Coordinated Restore at Checkpoint", 42),
    KUBERNETES("Kubernetes", 43),

    // CLOUD
    SERVERLESS("Serverless", 44),
    AWS_LAMBDA("AWS Lambda", 45),
    SCALE_TO_ZERO_CONTAINERS("Scale to Zero Containers", 46),

    // SERVER-SIDE HTML
    VIEWS("Views", 47),
    TURBO("Turbo", 48),
    STATIC_RESOURCES("Static Resources", 49),

    // LANGUAGES
    KOTLIN("Kotlin", 50),
    GRAALPY("GraalPy", 51),

    // FRAMEWORKS
    SPRING("Spring Boot", 52),
    SPRING_BOOT_TO_MICRONAUT_BUILDING_A_REST_API("Boot to Micronaut Building a REST API", 53);

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