package io.micronaut.guides.feature;

import io.micronaut.starter.build.dependencies.Scope;
import jakarta.inject.Singleton;

@Singleton
public class WiremockTestcontainersJava extends AbstractFeature implements RequiresJitpack {
    public WiremockTestcontainersJava() {
        super("wiremock-testcontainers-java", "wiremock-testcontainers-java", Scope.TEST);
    }


}
