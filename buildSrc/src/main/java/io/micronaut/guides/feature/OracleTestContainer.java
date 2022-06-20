package io.micronaut.guides.feature;

import io.micronaut.starter.build.dependencies.Scope;
import jakarta.inject.Singleton;

@Singleton
public class OracleTestContainer extends AbstractFeature {
    public OracleTestContainer() {
        super("testcontainers-oracle", "oracle-xe", Scope.TEST);
    }
}
