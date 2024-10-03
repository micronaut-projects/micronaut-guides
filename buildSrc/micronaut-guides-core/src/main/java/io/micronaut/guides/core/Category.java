package io.micronaut.guides.core;

import io.micronaut.core.order.Ordered;

public interface Category extends Ordered {
    String getName();
    int getOrder();
}
