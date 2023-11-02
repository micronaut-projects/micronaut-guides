package io.micronaut.guides;

import io.micronaut.core.order.Ordered;

public enum Category implements Ordered {
    GETTING_STARTED("Getting Started", 1),
    BEYOND_THE_BASICS("Beyond the Basics", 2),
    DISTRIBUTION("Distribution", 3),

    GRAALVM("GraalVM", 4),
    SCHEMA_MIGRATION("Schema Migration", 5),
    DATA_ACCESS("Data Access", 6),
    CACHE("Cache", 7),
    TEST("Testing", 8),

    VIEWS("Views", 9),
    SECURITY("Micronaut Security",10),
    AUTHORIZATION_CODE("Authorization Code",11),
    CLIENT_CREDENTIALS("Client Credentials",12),
    SECRETS_MANAGER("Secrets Manager", 13),
    PATTERNS("Patterns", 14),
    EMAIL("Email", 15),
    KOTLIN("Kotlin", 16),
    MESSAGING("Messaging", 17),
    API("API", 18),
    OPEN_API("OpenAPI", 19),
    TURBO("Turbo", 20),
    GRAPHQL("GraphQL", 21),
    METRICS("Metrics", 22),
    DISTRIBUTED_TRACING("Distributed Tracing", 23),
    SERVICE_DISCOVERY("Service Discovery", 24),

    DISTRIBUTED_CONFIGURATION("Distributed Configuration", 25),
    COMMON_TASKS("Common Tasks", 26),
    OBJECT_STORAGE("Object Storage", 27),
    AWS("AWS", 28),
    AWS_LAMBDA("AWS Lambda", 29),
    AZURE("Microsoft Azure", 30),

    GCP("Google Cloud", 31),
    GOOGLE_CLOUD_RUN("Google Cloud Run", 32),
    ORACLE_CLOUD("Oracle Cloud", 33),
    KUBERNETES( "Kubernetes", 34),
    SPRING_BOOT_TO_MICRONAUT("Spring Boot to Micronaut Framework", 35);



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
