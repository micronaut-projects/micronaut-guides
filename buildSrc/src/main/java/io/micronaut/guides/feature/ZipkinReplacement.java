package io.micronaut.guides.feature;

import io.micronaut.context.annotation.Replaces;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.build.dependencies.MicronautDependencyUtils;
import io.micronaut.starter.feature.tracing.Zipkin;
import jakarta.inject.Singleton;
// Delete this class once this is fixed https://github.com/micronaut-projects/micronaut-starter/pull/1937
@Replaces(Zipkin.class)
@Singleton
public class ZipkinReplacement extends Zipkin {
    private static final String ARTIFACT_ID_MICRONAUT_TRACING_BRAVE_HTTP = "micronaut-tracing-brave-http";
    @Override
    public void apply(GeneratorContext generatorContext) {
        addDependencies(generatorContext);
        generatorContext.getConfiguration().put("tracing.zipkin.enabled", true);
        generatorContext.getConfiguration().put("tracing.zipkin.http.url", "http://localhost:9411");
        generatorContext.getConfiguration().put("tracing.zipkin.sampler.probability", 0.1);
    }

    protected void addDependencies(GeneratorContext generatorContext) {
        generatorContext.addDependency(MicronautDependencyUtils.tracingDependency()
                .artifactId(ARTIFACT_ID_MICRONAUT_TRACING_BRAVE_HTTP)
                .compile());
    }
}
