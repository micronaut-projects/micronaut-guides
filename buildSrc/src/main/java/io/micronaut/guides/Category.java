package io.micronaut.guides;

public enum Category {
    API("API"),
    AWS("AWS"),
    AZURE("Micronaut + Microsoft Azure"),
    CACHE("Cache"),
    DATA_ACCESS("Data Access"),
    DISTRIBUTED_TRACING("Distributed Tracing"),
    EMAIL("Email"),
    GCP("Micronaut + Google Cloud"),
    GETTING_STARTED("Getting Started"),
    GRAPHQL("GraphQL"),
    KOTLIN("Kotlin"),
    MESSAGING("Messaging"),
    ORACLE_CLOUD("Micronaut + Oracle Cloud"),
    PATTERNS("Patterns"),
    SECURITY("Micronaut Security"),
    SERVICE_DISCOVERY("Service Discovery"),
    TEST("Testing");

    private final String val;

    Category(String val) {
        this.val = val;
    }

    @Override
    public String toString() {
        return val;
    }
}
