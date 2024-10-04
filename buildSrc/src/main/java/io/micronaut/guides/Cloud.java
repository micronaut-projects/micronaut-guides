package io.micronaut.guides;

import io.micronaut.core.order.Ordered;

public enum Cloud implements Ordered {
    OCI("Oracle Cloud", "OCI", 1),
    AWS("Amazon Web Services",  "AWS", 2),
    AZURE("Microsoft Azure", "Azure", 3),
    GCP("Google Cloud Platform", "GCP", 4);

    private final String accronym;
    private final String name;
    private final int order;

    Cloud(String name, String accronym, int order) {
        this.name = name;
        this.accronym = accronym;
        this.order = order;
    }

    public String getName() {
        return name;
    }

    public String getAccronym() {
        return accronym;
    }

    @Override
    public String toString() {
        return accronym;
    }

    @Override
    public int getOrder() {
        return order;
    }
}
