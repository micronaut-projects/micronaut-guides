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
    CACHE("Cache", 8),
    TEST("Testing", 9),

    VIEWS("Views", 10),
    SECURITY("Micronaut Security",11),
    AUTHORIZATION_CODE("Authorization Code",12),
    CLIENT_CREDENTIALS("Client Credentials",13),
    SECRETS_MANAGER("Secrets Manager", 14),
    PATTERNS("Patterns", 15),
    EMAIL("Email", 16),
    KOTLIN("Kotlin", 17),
    MESSAGING("Messaging", 18),
    API("API", 19),
    OPEN_API("OpenAPI", 20),
    TURBO("Turbo", 21),
    GRAPHQL("GraphQL", 22),
    METRICS("Metrics", 23),
    DISTRIBUTED_TRACING("Distributed Tracing", 24),
    SERVICE_DISCOVERY("Service Discovery", 26),
    DISTRIBUTED_CONFIGURATION("Distributed Configuration", 26),
    COMMON_TASKS("Common Tasks", 27),
    OBJECT_STORAGE("Object Storage", 28),
    AWS("AWS", 29),
    AWS_LAMBDA("AWS Lambda", 30),
    AZURE("Microsoft Azure", 31),
    GCP("Google Cloud", 32),
    GOOGLE_CLOUD_RUN("Google Cloud Run", 33),
    ORACLE_CLOUD("Oracle Cloud", 34),
    KUBERNETES( "Kubernetes", 35),
    SPRING_BOOT_TO_MICRONAUT("Spring Boot to Micronaut Framework", 36);

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
