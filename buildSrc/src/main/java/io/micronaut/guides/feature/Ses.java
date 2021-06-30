package io.micronaut.guides.feature;

import javax.inject.Singleton;

@Singleton
public class Ses extends AbstractFeature {

    public Ses() {
        super("ses");
    }
}
