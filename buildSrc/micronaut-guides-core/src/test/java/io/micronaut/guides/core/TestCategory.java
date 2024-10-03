package io.micronaut.guides.core;

import io.micronaut.starter.feature.function.Cloud;

public enum TestCategory implements  Category {
    TEST_CATEGORY("Test Category", 1),
    CLOUD("Cloud",2);

    private final String name;
    private final int order;

    TestCategory(String name, int order) {
        this.name = name;
        this.order = order;
    }


    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getOrder() {
        return order;
    }
}
