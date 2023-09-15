package io.micronaut.guides.feature;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.build.dependencies.Dependency;
import io.micronaut.starter.feature.Feature;
import io.micronaut.starter.feature.database.TestContainers;
import jakarta.inject.Singleton;

@Singleton
public class MockServerClient implements Feature {

    public static final String ARTIFACT_ID_MOCKSERVER_CLIENT_JAVA = "mockserver-client-java";
    public static final String ARTIFACT_ID_TEST_CONTAINERS_MOCKSERVER = "mockserver";

    @Override
    public @NonNull String getName() {
        return "mockserver-client-java";
    }

    @Override
    public boolean supports(ApplicationType applicationType) {
        return true;
    }

    @Override
    public @Nullable String getThirdPartyDocumentation() {
        return "https://www.mock-server.com/mock_server/mockserver_clients.html#java-mockserver-client";
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        addDependencies(generatorContext);
    }

    protected void addDependencies(GeneratorContext generatorContext) {
        if (generatorContext.getFeatures().hasFeature(TestContainers.class)) {
            generatorContext.addDependency(Dependency.builder().groupId(TestContainers.TESTCONTAINERS_GROUP_ID).artifactId(ARTIFACT_ID_TEST_CONTAINERS_MOCKSERVER).test().build());
        }
        generatorContext.addDependency(Dependency.builder().lookupArtifactId(ARTIFACT_ID_MOCKSERVER_CLIENT_JAVA).test());
    }
}