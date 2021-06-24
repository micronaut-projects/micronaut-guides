package io.micronaut.guides.feature;

import javax.inject.Singleton;

import static io.micronaut.starter.build.dependencies.Scope.RUNTIME;

@Singleton
public class OracleCloudAtpFeature extends AbstractFeature {

    public OracleCloudAtpFeature() {
        super("micronaut-oraclecloud-atp", RUNTIME);
    }
}
