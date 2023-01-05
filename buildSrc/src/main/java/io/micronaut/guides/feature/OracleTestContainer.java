package io.micronaut.guides.feature;

import jakarta.inject.Singleton;

import static io.micronaut.starter.build.dependencies.Scope.TEST;

@Singleton
public class OracleTestContainer extends AbstractFeature {
    public OracleTestContainer() {
        super("testcontainers-oracle", "oracle-xe", TEST);
    }
}
