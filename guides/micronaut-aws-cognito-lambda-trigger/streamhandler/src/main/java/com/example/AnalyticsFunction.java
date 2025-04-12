package com.example;

import io.micronaut.function.FunctionBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Consumer;
import java.util.function.Function;

@FunctionBean("analytics")
public class AnalyticsFunction implements Consumer<String> {
    private static final Logger LOG = LoggerFactory.getLogger(AnalyticsFunction.class);

    @Override
    public void accept(String username) {
        LOG.info("username {}", username);
    }
}
