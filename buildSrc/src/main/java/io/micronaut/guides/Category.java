package io.micronaut.guides;

import io.micronaut.core.order.Ordered;

public enum Category implements Ordered {
    GETTING_STARTED("Getting Started", 1),
    BEYOND_THE_BASICS("Beyond the Basics", 2),
    DISTRIBUTION("Distribution", 3),
    GRAALVM("GraalVM", 4),
    CRAC("CRaC", 5),
    SCHEMA_MIGRATION("Schema Migration", 6),
    MICRONAUT_DATA("Micronaut Data", 7),
    DATA_ACCESS("Data Access", 8),
    CACHE("Cache", 9),
    TEST("Testing", 10),

    VIEWS("Views", 11),
    SECURITY("Micronaut Security",12),
    AUTHORIZATION_CODE("Authorization Code",13),
    CLIENT_CREDENTIALS("Client Credentials",14),
    SECRETS_MANAGER("Secrets Manager", 15),
    PATTERNS("Patterns", 16),
    EMAIL("Email", 17),
    KOTLIN("Kotlin", 18),
    MESSAGING("Messaging", 19),
    API("API", 20),
    OPEN_API("OpenAPI", 21),
    TURBO("Turbo", 22),
    GRAPHQL("GraphQL", 23),
    METRICS("Metrics", 24),
    DISTRIBUTED_TRACING("Distributed Tracing", 25),
    SERVICE_DISCOVERY("Service Discovery", 26),
    DISTRIBUTED_CONFIGURATION("Distributed Configuration", 27),
    COMMON_TASKS("Common Tasks", 28),
    OBJECT_STORAGE("Object Storage", 29),
    AWS("AWS", 30),
    AWS_LAMBDA("AWS Lambda", 31),
    AZURE("Microsoft Azure", 32),
    GCP("Google Cloud", 33),
    GOOGLE_CLOUD_RUN("Google Cloud Run", 34),
    ORACLE_CLOUD("Oracle Cloud", 35),
    GRAALPY("GraalPy", 36),
    KUBERNETES( "Kubernetes", 37),
    SPRING_BOOT_TO_MICRONAUT("Spring Boot to Micronaut Framework", 38),
    BUILDING_A_REST_API("Building a REST API - Spring Boot to Micronaut Framework", 39);

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
