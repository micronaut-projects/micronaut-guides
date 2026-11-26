package io.micronaut.guides.feature;

import io.micronaut.starter.application.generator.GeneratorContext;
import jakarta.inject.Singleton;

import static io.micronaut.starter.build.dependencies.Scope.TEST;

/**
 * Adds the Micronaut MCP Server Java SDK with test scope, to start an MCP server in the tests of an MCP client.
 */
@Singleton
public class McpServerJavaSdkTest extends AbstractFeature {

    public McpServerJavaSdkTest() {
        super("mcp-server-java-sdk-test", "micronaut-mcp-server-java-sdk", TEST);
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        addDependencyWithoutLookup(generatorContext, "io.micronaut.mcp");
    }
}
