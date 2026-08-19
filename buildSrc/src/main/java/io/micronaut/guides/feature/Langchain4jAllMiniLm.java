package io.micronaut.guides.feature;

import io.micronaut.starter.application.generator.GeneratorContext;
import jakarta.inject.Singleton;

@Singleton
public class Langchain4jAllMiniLm extends AbstractFeature {

    public Langchain4jAllMiniLm() {
        super("langchain4j-all-minilm", "langchain4j-embeddings-all-minilm-l6-v2");
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        addDependencyWithoutLookup(generatorContext, "dev.langchain4j");
    }
}
