package io.micronaut.guides;

import io.micronaut.core.order.Ordered;

public enum Category implements Ordered {
    GETTING_STARTED("Getting Started", 1),
    BEYOND_THE_BASICS("Beyond the Basics", 2),

    SPRING_BOOT_TO_MICRONAUT("Spring Boot to Micronaut Framework", 3),

    SCHEMA_MIGRATION("Schema Migration", 4),
    DATA_ACCESS("Data Access", 5),
    CACHE("Cache", 6),
    TEST("Testing", 7),
    SECURITY("Micronaut Security",8),
    AUTHORIZATION_CODE("Authorization Code",9),
    CLIENT_CREDENTIALS("Client Credentials",10),
    SECRETS_MANAGER("Secrets Manager", 11),
    PATTERNS("Patterns", 12),
    EMAIL("Email", 13),
    KOTLIN("Kotlin", 14),
    MESSAGING("Messaging", 15),
    API("API", 16),
    OPEN_API("OpenAPI", 17),
    TURBO("Turbo", 18),
    GRAPHQL("GraphQL", 19),
    METRICS("Metrics", 20),
    DISTRIBUTED_TRACING("Distributed Tracing", 21),
    SERVICE_DISCOVERY("Service Discovery", 22),
    COMMON_TASKS("Common Tasks", 23),
    AWS("AWS", 24),
    AWS_LAMBDA("AWS Lambda", 25),
    AZURE("Micronaut + Microsoft Azure", 26),

    GCP("Micronaut + Google Cloud", 27),
    ORACLE_CLOUD("Micronaut + Oracle Cloud", 28);

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
