package io.micronaut.guides.feature;

import javax.inject.Singleton;

@Singleton
public class Sendgrid extends AbstractFeature {

    public Sendgrid() {
        super("sendgrid", "sendgrid-java");
    }
}
