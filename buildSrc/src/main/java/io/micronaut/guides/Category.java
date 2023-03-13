package io.micronaut.guides;

import io.micronaut.core.order.Ordered;

public enum Category implements Ordered {
    GETTING_STARTED("Getting Started", 1),
    BEYOND_THE_BASICS("Beyond the Basics", 2),
    DISTRIBUTION("Distribution", 3),

    SPRING_BOOT_TO_MICRONAUT("Spring Boot to Micronaut Framework", 4),

    SCHEMA_MIGRATION("Schema Migration", 5),
    DATA_ACCESS("Data Access", 6),
    CACHE("Cache", 7),
    TEST("Testing", 8),
    SECURITY("Micronaut Security",9),
    AUTHORIZATION_CODE("Authorization Code",10),
    CLIENT_CREDENTIALS("Client Credentials",11),
    SECRETS_MANAGER("Secrets Manager", 12),
    PATTERNS("Patterns", 13),
    EMAIL("Email", 14),
    KOTLIN("Kotlin", 15),
    MESSAGING("Messaging", 16),
    API("API", 17),
    OPEN_API("OpenAPI", 18),
    TURBO("Turbo", 19),
    GRAPHQL("GraphQL", 20),
    METRICS("Metrics", 21),
    DISTRIBUTED_TRACING("Distributed Tracing", 22),
    SERVICE_DISCOVERY("Service Discovery", 23),
    COMMON_TASKS("Common Tasks", 24),
    OBJECT_STORAGE("Object Storage", 25),
    CHATBOTS("ChatBots", 26),
    AWS("AWS", 27),
    AWS_LAMBDA("AWS Lambda", 28),
    AZURE("Micronaut + Microsoft Azure", 29),
    GCP("Micronaut + Google Cloud", 30),
    ORACLE_CLOUD("Micronaut + Oracle Cloud", 31);

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
