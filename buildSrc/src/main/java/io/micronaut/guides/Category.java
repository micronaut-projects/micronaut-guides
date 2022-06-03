package io.micronaut.guides;

public enum Category {
    API("API"),
    GETTING_STARTED("Getting Started"),
    EMAIL("Email"),
    DATA_ACCESS("Data Access"),
    CACHE("Cache"),
    GRAPHQL("GraphQL"),
    MESSAGING("Messaging"),
    SECURITY("Micronaut Security"),
    SERVICE_DISCOVERY("Service Discovery"),
    DISTRIBUTED_TRACING("Distributed Tracing"),
    KOTLIN("Kotlin"),
    AWS("AWS"),
    AZURE("Micronaut + Microsoft Azure"),
    GCP("Micronaut + Google Cloud"),
    TEST("Testing"),
    ORACLE_CLOUD("Micronaut + Oracle Cloud"),
    PATTERNS("Patterns");

    private final String val;

    Category(String val) {
        this.val = val;
    }

    @Override
    public String toString() {
        return val;
    }
}
