package io.micronaut.guides.feature;

import jakarta.inject.Singleton;

@Singleton
public class Thumbnailator extends AbstractFeature {

    public Thumbnailator() {
        super("thumbnailator");
    }
}
