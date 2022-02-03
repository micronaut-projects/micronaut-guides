package io.micronaut.guides.feature;

import jakarta.inject.Singleton;

@Singleton
public class FlywayMySQL extends AbstractFeature {
    protected FlywayMySQL() {
        super("flyway-mysql");
    }
}
