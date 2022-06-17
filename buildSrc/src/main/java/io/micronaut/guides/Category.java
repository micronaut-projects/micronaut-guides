package io.micronaut.guides;

import io.micronaut.core.order.Ordered;

public enum Category implements Ordered {
    GETTING_STARTED("Getting Started", 1),
    DATA_ACCESS("Data Access", 2),
    CACHE("Cache", 3),
    TEST("Testing", 4),
    SECURITY("Micronaut Security",5),
    EMAIL("Email", 6),
    KOTLIN("Kotlin", 7),
    MESSAGING("Messaging", 8),
    API("API", 9),
    GRAPHQL("GraphQL", 10),
    METRICS("Metrics", 11),
    DISTRIBUTED_TRACING("Distributed Tracing", 12),
    SERVICE_DISCOVERY("Service Discovery", 13),
    PATTERNS("Patterns", 14),
    AWS("AWS", 15),
    AZURE("Micronaut + Microsoft Azure", 16),
    GCP("Micronaut + Google Cloud", 17),
    ORACLE_CLOUD("Micronaut + Oracle Cloud", 18);

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
