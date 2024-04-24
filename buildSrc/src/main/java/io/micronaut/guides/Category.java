package io.micronaut.guides;

import io.micronaut.core.order.Ordered;

public enum Category implements Ordered {
    GETTING_STARTED("Getting Started", 1),
    BEYOND_THE_BASICS("Beyond the Basics", 2),
    DISTRIBUTION("Distribution", 3),
    GRAALVM("GraalVM", 4),
    CRAC("CRaC", 5),
    SCHEMA_MIGRATION("Schema Migration", 6),
    DATA_ACCESS("Data Access", 7),
    MICRONAUT_DATA("Micronaut Data", 8),
    MULTI_TENANCY("Multi-Tenancy", 9),
    CACHE("Cache", 10),
    TEST("Testing", 11),
    VIEWS("Views", 12),
    SECURITY("Micronaut Security",13),
    AUTHORIZATION_CODE("Authorization Code",14),
    CLIENT_CREDENTIALS("Client Credentials",15),
    SECRETS_MANAGER("Secrets Manager", 16),
    PATTERNS("Patterns", 17),
    EMAIL("Email", 18),
    KOTLIN("Kotlin", 19),
    MESSAGING("Messaging", 20),
    API("API", 21),
    OPEN_API("OpenAPI", 22),
    TURBO("Turbo", 23),
    GRAPHQL("GraphQL", 24),
    METRICS("Metrics", 25),
    DISTRIBUTED_TRACING("Distributed Tracing", 26),
    SERVICE_DISCOVERY("Service Discovery", 27),
    DISTRIBUTED_CONFIGURATION("Distributed Configuration", 28),
    COMMON_TASKS("Common Tasks", 28),
    OBJECT_STORAGE("Object Storage", 29),
    AWS("AWS", 30),
    AWS_LAMBDA("AWS Lambda", 31),
    AZURE("Microsoft Azure", 32),
    GCP("Google Cloud", 33),
    GOOGLE_CLOUD_RUN("Google Cloud Run", 34),
    ORACLE_CLOUD("Oracle Cloud", 35),
    KUBERNETES( "Kubernetes", 36),
    SPRING_BOOT_TO_MICRONAUT("Spring Boot to Micronaut Framework", 37);

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
