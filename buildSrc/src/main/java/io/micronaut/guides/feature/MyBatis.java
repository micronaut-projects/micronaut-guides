package io.micronaut.guides.feature;

import jakarta.inject.Singleton;

@Singleton
public class MyBatis extends AbstractFeature {

    public MyBatis() {
        super("mybatis");
    }
}
