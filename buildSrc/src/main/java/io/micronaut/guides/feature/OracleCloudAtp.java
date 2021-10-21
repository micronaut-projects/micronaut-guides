package io.micronaut.guides.feature;

import jakarta.inject.Singleton;

@Singleton
public class OracleCloudAtp extends AbstractFeature {
    public OracleCloudAtp() {
        super("micronaut-oracle-cloud-atp", "micronaut-oraclecloud-atp");
    }
}
