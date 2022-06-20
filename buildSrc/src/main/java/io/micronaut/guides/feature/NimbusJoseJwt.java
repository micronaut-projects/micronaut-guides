package io.micronaut.guides.feature;

import jakarta.inject.Singleton;

@Singleton
public class NimbusJoseJwt extends AbstractFeature {

    public NimbusJoseJwt() {
        super("nimbus-jose-jwt");
    }
}
