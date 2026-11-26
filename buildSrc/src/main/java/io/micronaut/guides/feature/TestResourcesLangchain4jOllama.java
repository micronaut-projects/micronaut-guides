package io.micronaut.guides.feature;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.build.dependencies.Dependency;
import io.micronaut.starter.build.dependencies.MavenCoordinate;
import io.micronaut.starter.feature.Feature;
import io.micronaut.starter.feature.FeaturePhase;
import io.micronaut.starter.feature.testresources.TestResourcesAdditionalModulesProvider;
import io.micronaut.starter.options.BuildTool;
import jakarta.inject.Singleton;

import java.util.Collections;
import java.util.List;

/**
 * Registers the Micronaut LangChain4j Ollama test resource, which starts an Ollama container with Testcontainers.
 * <p>
 * The {@code langchain4j-ollama} Starter feature adds a non-existent {@code micronaut-langchain4j-ollama-testresources}
 * artifact to Gradle and Pyronaut builds and nothing to Maven builds. This feature replaces it with the published
 * {@code micronaut-langchain4j-ollama-testresource} artifact for every build tool.
 */
@Singleton
public class TestResourcesLangchain4jOllama implements Feature, TestResourcesAdditionalModulesProvider {

    private static final String NAME = "test-resources-langchain4j-ollama";
    private static final String GROUP_ID = "io.micronaut.langchain4j";
    private static final String STARTER_ARTIFACT_ID = "micronaut-langchain4j-ollama-testresources";
    private static final String ARTIFACT_ID = "micronaut-langchain4j-ollama-testresource";
    private static final MavenCoordinate MAVEN_DEPENDENCY = new MavenCoordinate(
            GROUP_ID,
            ARTIFACT_ID,
            "${micronaut.langchain4j.version}");

    @Override
    @NonNull
    public String getName() {
        return NAME;
    }

    @Override
    public boolean supports(ApplicationType applicationType) {
        return true;
    }

    @Override
    public int getOrder() {
        return FeaturePhase.DEFAULT.getOrder() + 1;
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        if (generatorContext.getBuildTool() != BuildTool.MAVEN) {
            generatorContext.getDependencies().removeIf(dependency -> STARTER_ARTIFACT_ID.equals(dependency.getArtifactId()));
            generatorContext.addDependency(Dependency.builder()
                    .groupId(GROUP_ID)
                    .artifactId(ARTIFACT_ID)
                    .testResourcesService());
        }
    }

    @Override
    public List<String> getTestResourcesAdditionalModules(GeneratorContext generatorContext) {
        return Collections.emptyList();
    }

    @Override
    public List<MavenCoordinate> getTestResourcesDependencies(GeneratorContext generatorContext) {
        if (generatorContext.getBuildTool() == BuildTool.MAVEN) {
            return List.of(MAVEN_DEPENDENCY);
        }
        return Collections.emptyList();
    }
}
