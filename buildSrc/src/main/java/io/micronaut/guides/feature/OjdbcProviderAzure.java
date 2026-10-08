package io.micronaut.guides.feature;

import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.build.dependencies.Dependency;
import jakarta.inject.Singleton;

/**
 * Oracle JDBC provider for Azure: authenticates JDBC connections with Microsoft Entra ID tokens.
 * The Azure SDK Netty HTTP client is excluded because it conflicts with the Netty version shipped by Micronaut.
 */
@Singleton
public class OjdbcProviderAzure extends AbstractFeature {

    public OjdbcProviderAzure() {
        super("ojdbc-provider-azure");
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        generatorContext.addDependency(Dependency.builder()
                .groupId("com.oracle.database.jdbc")
                .artifactId("ojdbc-provider-azure")
                .version("1.1.0")
                .compile()
                .exclude(Dependency.builder()
                        .groupId("com.azure")
                        .artifactId("azure-core-http-netty")
                        .build())
                .build());
    }
}
