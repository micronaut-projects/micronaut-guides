package io.micronaut.guides.feature;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.build.dependencies.Dependency;
import io.micronaut.starter.feature.Feature;
import javax.inject.Singleton;
import static io.micronaut.starter.build.dependencies.Scope.TEST;

@Singleton
public class JunitParams extends AbstractFeature {
    public JunitParams() {
        super("junit-params", "junit-jupiter-params", TEST);
    }
}
