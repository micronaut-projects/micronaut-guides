package io.micronaut.guides.feature;

import jakarta.inject.Singleton;

@Singleton
public class OracledbMicrotx extends AbstractFeature {

    public OracledbMicrotx() {
        super("oracledb-microtx", "micronaut-oracledb-microtx");
    }
}
