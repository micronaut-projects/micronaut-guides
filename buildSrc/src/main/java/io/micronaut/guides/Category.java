package io.micronaut.guides;

public enum Category {

    AWS("AWS"),
    AZURE("Micronaut + Microsoft Azure"),
    CACHE("Cache"),
    DATA_ACCESS("Data Access"),
    SERVICE_DISCOVERY("Service Discovery"),
    SECURITY("Micronaut Security"),
    MESSAGING("Messaging"),
    KOTLIN("Kotlin"),
    DISTRIBUTED_TRACING("Distributed Tracing"),
    APPRENTICE("Micronaut Apprentice");

    private String val;

    Category(String val) {
        this.val = val;
    }

    @Override
    public String toString() {
        return val;
    }
}
