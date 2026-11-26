package io.micronaut.guides.feature;

import io.micronaut.starter.application.generator.GeneratorContext;
import jakarta.inject.Singleton;

/**
 * Adds the LangChain4j in-process all-MiniLM-L6-v2 embedding model. The version is managed by the LangChain4j BOM,
 * which the Micronaut platform imports through the Micronaut LangChain4j BOM.
 */
@Singleton
public class Langchain4jEmbeddingsAllMiniLmL6V2 extends AbstractFeature {

    public Langchain4jEmbeddingsAllMiniLmL6V2() {
        super("langchain4j-embeddings-all-minilm-l6-v2", "langchain4j-embeddings-all-minilm-l6-v2");
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        addDependencyWithoutLookup(generatorContext, "dev.langchain4j");
    }
}
