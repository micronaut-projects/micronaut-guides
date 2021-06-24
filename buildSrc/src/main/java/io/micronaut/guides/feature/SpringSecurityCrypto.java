package io.micronaut.guides.feature;

import javax.inject.Singleton;

@Singleton
public class SpringSecurityCrypto extends AbstractFeature {

    public SpringSecurityCrypto() {
        super("spring-security-crypto");
    }
}
