package io.micronaut.guides.feature;

import io.micronaut.starter.application.generator.GeneratorContext;
import jakarta.inject.Singleton;

import static io.micronaut.starter.build.dependencies.Scope.COMPILE_ONLY;

@Singleton
public class HttpValidationCompileOnlySupport extends AbstractFeature {

    public HttpValidationCompileOnlySupport() {
        super("http-validation-compile-only-support", "micronaut-router", COMPILE_ONLY);
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        super.apply(generatorContext);
        addDependency(generatorContext, "micronaut-http-server", COMPILE_ONLY);
    }
}
