package io.micronaut.guides.feature;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.build.dependencies.Dependency;
import io.micronaut.starter.feature.Feature;
import io.micronaut.starter.feature.FeaturePhase;
import jakarta.inject.Singleton;

/**
 * The {@code mcp-client-langchain4j} Starter feature adds the Micronaut MCP Client LangChain4j module with test scope,
 * to test MCP servers. This feature moves it to the compile scope, for applications that call the tools of MCP servers.
 */
@Singleton
public class McpClientLangchain4jCompile implements Feature {

    private static final String NAME = "mcp-client-langchain4j-compile";
    private static final String GROUP_ID = "io.micronaut.mcp";
    private static final String ARTIFACT_ID = "micronaut-mcp-client-langchain4j";

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
        generatorContext.getDependencies().removeIf(dependency -> ARTIFACT_ID.equals(dependency.getArtifactId()));
        generatorContext.addDependency(Dependency.builder()
                .groupId(GROUP_ID)
                .artifactId(ARTIFACT_ID)
                .compile());
    }
}
