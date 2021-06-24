package io.micronaut.guides.feature;

import javax.inject.Singleton;

@Singleton
public class Thumbnailator extends AbstractFeature {

    public Thumbnailator() {
        super("thumbnailator");
    }
}
