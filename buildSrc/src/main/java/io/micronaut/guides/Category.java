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
    SCHEDULING("Scheduling", 9),
    CACHE("Cache", 10),
    PATTERNS("Patterns", 11),
    INTERNATIONALIZATION("i18n", 12),

    // DATA ACCESS
    DATA_JDBC("Data JDBC", 13),
    DATA_JPA("Data JPA", 14),
    SCHEMA_MIGRATION("Schema Migration", 15),
    DATA_RDBC("Data R2DBC", 16),
    DATA_MONGO("MongoDB", 17),
    DATA_ACCESS("Data Access", 18),

    // SECURITY
    SECURITY("Micronaut Security",19),
    AUTHORIZATION_CODE("Authorization Code",20),
    CLIENT_CREDENTIALS("Client Credentials",21),
    SECRETS_MANAGER("Secrets Manager", 22),

    // HTTP & API
    HTTP("HTTP Server", 23),
    HTTP_CLIENT("HTTP Client", 24),
    BEYOND_JSON("Beyond JSON", 25),
    JAX_RS("JAX-RS", 26),
    WEBSOCKETS("WebSockets", 27),
    GRAPHQL("GraphQL", 28),
    OPEN_API("OpenAPI", 29),
    JSON_SCHEMA("JSON Schema", 30),

    // DISTRIBUTED SYSTEMS
    DISTRIBUTED_TRACING("Distributed Tracing", 31),
    SERVICE_DISCOVERY("Service Discovery", 32),
    DISTRIBUTED_CONFIGURATION("Distributed Configuration", 33),
    METRICS("Metrics", 33),

    // DISTRIBUTION
    DISTRIBUTION("Distribution", 34),
    GRAALVM("GraalVM", 35),
    CRAC("Coordinated Restore at Checkpoint", 37),
    KUBERNETES( "Kubernetes", 38),

    // CLOUD
    SERVERLESS("Serverless", 39),
    AWS_LAMBDA("AWS Lambda", 40),
    SCALE_TO_ZERO_CONTAINERS("Scale to Zero Containers", 41),

    // SERVER-SIDE HTML
    VIEWS("Views", 42),
    TURBO("Turbo", 43),
    STATIC_RESOURCES("Static Resources", 44),

    // LANGUAGES
    KOTLIN("Kotlin", 45),
    GRAALPY("GraalPy", 46),

    // FRAMEWORKS
    SPRING("Spring Boot", 47),
    SPRING_BOOT_TO_MICRONAUT_BUILDING_A_REST_API("Boot to Micronaut Building a REST API", 48);

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
