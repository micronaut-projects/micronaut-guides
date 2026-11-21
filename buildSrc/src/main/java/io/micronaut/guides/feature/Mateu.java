package io.micronaut.guides.feature;

import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.build.dependencies.Dependency;
import jakarta.inject.Singleton;

/**
 * Mateu (https://mateu.io): {@code @UI} classes rendered as a web UI with Oracle Redwood.
 * Remove this class once the {@code mateu} feature ships in Micronaut Starter.
 */
@Singleton
public class Mateu extends AbstractFeature {

    public Mateu() {
        super("mateu", "mateu-micronaut");
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        super.apply(generatorContext);
        generatorContext.addDependency(Dependency.builder()
                .lookupArtifactId("mateu-redwood")
                .runtime());
        generatorContext.addDependency(Dependency.builder()
                .lookupArtifactId("mateu-annotation-processor-micronaut")
                .annotationProcessor());
    }
}
