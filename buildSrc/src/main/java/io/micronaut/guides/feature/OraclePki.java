package io.micronaut.guides.feature;

import jakarta.inject.Singleton;

/**
 * Oracle PKI: reads an Oracle wallet ({@code cwallet.sso}) to validate the database certificate over TCPS.
 */
@Singleton
public class OraclePki extends AbstractFeature {

    public OraclePki() {
        super("oraclepki");
    }
}
