package io.micronaut.guides.feature;

import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.build.dependencies.Coordinate;
import jakarta.inject.Singleton;

@Singleton
public class MicronautLangchain4jOpenAi extends AbstractFeature {
    private static final String ARTIFACT_ID_MICRONAUT_LANGCHAIN_4_J_OPENAI = "micronaut-langchain4j-openai";

    public MicronautLangchain4jOpenAi() {
        super(ARTIFACT_ID_MICRONAUT_LANGCHAIN_4_J_OPENAI);
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        super.apply(generatorContext);
        Coordinate coordinate = generatorContext.resolveCoordinate(ARTIFACT_ID_MICRONAUT_LANGCHAIN_4_J_OPENAI);
        generatorContext.getBuildProperties().put("micronaut.langchain4j.version", coordinate.getVersion());
    }
}
