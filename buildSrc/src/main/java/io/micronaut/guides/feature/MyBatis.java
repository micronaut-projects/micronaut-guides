package io.micronaut.guides.feature;

import javax.inject.Singleton;

@Singleton
public class MyBatis extends AbstractFeature {

    public MyBatis() {
        super("mybatis");
    }
}
