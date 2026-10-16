package io.micronaut.guides.feature;

import jakarta.inject.Singleton;

@Singleton
public class OracleCache extends AbstractFeature {

    public OracleCache() {
        super("cache-oracle", "micronaut-cache-oracle");
    }
}
