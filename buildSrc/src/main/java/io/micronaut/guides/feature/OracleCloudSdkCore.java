package io.micronaut.guides.feature;

import io.micronaut.guides.feature.AbstractFeature;

import javax.inject.Singleton;

@Singleton
public class OracleCloudSdkCore extends AbstractFeature {

    public OracleCloudSdkCore() {
        super("oci-java-sdk-core");
    }
}
