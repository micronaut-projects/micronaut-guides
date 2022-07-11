package io.micronaut.guides;

import io.micronaut.core.order.Ordered;

public enum Category implements Ordered {
    GETTING_STARTED("Getting Started", 1),
    BEYOND_THE_BASICS("Beyond the Basics", 2),
    SCHEMA_MIGRATION("Schema Migration", 3),
    DATA_ACCESS("Data Access", 4),
    CACHE("Cache", 5),
    TEST("Testing", 6),
    SECURITY("Micronaut Security",7),
    AUTHORIZATION_CODE("Authorization Code",8),
    CLIENT_CREDENTIALS("Client Credentials",9),
    SECRETS_MANAGER("Secrets Manager", 10),
    PATTERNS("Patterns", 11),
    EMAIL("Email", 12),
    KOTLIN("Kotlin", 13),
    MESSAGING("Messaging", 14),
    API("API", 15),
    OPEN_API("OpenAPI", 16),
    TURBO("Turbo", 17),
    GRAPHQL("GraphQL", 18),
    METRICS("Metrics", 19),
    DISTRIBUTED_TRACING("Distributed Tracing", 20),
    SERVICE_DISCOVERY("Service Discovery", 21),
    COMMON_TASKS("Common Tasks", 22),
    AWS("AWS", 23),
    AWS_LAMBDA("AWS Lambda", 24),
    AZURE("Micronaut + Microsoft Azure", 25),
    GCP("Micronaut + Google Cloud", 26),
    ORACLE_CLOUD("Micronaut + Oracle Cloud", 27);

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
