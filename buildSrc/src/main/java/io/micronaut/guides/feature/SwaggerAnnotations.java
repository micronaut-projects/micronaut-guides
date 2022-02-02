package io.micronaut.guides.feature;

import jakarta.inject.Singleton;

import static io.micronaut.starter.build.dependencies.Scope.TEST;

@Singleton
public class SwaggerAnnotations extends AbstractFeature {
    public SwaggerAnnotations() {
        super("swagger-annotations", "swagger-annotations");
    }
}
