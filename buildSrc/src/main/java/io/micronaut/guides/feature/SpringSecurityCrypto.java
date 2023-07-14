package io.micronaut.guides.feature;

import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.build.dependencies.Dependency;
import io.micronaut.starter.build.dependencies.Scope;
import jakarta.inject.Singleton;

@Singleton
public class SpringSecurityCrypto extends AbstractFeature {

    public SpringSecurityCrypto() {
        super("spring-security-crypto");
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        super.apply(generatorContext);

        generatorContext.addDependency(Dependency.builder()
                        .groupId("org.slf4j")
                        .artifactId("jcl-over-slf4j")
                        .scope(Scope.RUNTIME)
                .build());
    }
}
