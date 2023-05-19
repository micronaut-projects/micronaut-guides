package io.micronaut.guides.feature;

import io.micronaut.starter.application.generator.GeneratorContext;
import jakarta.inject.Singleton;

@Singleton
public class OracleCloudSdkCore extends AbstractFeature {

    public OracleCloudSdkCore() {
        super("oci-java-sdk-core");
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        addDependencyWithoutLookup(generatorContext, "com.oracle.oci.sdk");
    }
}
