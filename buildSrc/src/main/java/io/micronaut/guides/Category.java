package io.micronaut.guides;

public enum Category {
    APPRENTICE("Micronaut Apprentice"),
    DATA_ACCESS("Data Access"),
    CACHE("Cache"),
    MESSAGING("Messaging"),
    SECURITY("Micronaut Security"),
    SERVICE_DISCOVERY("Service Discovery"),
    DISTRIBUTED_TRACING("Distributed Tracing"),
    KOTLIN("Kotlin"),
    AWS("AWS"),
    AZURE("Micronaut + Microsoft Azure"),
    GCP("Micronaut + Google Cloud"),
    ORACLE_CLOUD("Micronaut + Oracle Cloud");

    private final String val;

    Category(String val) {
        this.val = val;
    }

    @Override
    public String toString() {
        return val;
    }
}
