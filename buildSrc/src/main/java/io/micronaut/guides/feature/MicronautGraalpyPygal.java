package io.micronaut.guides.feature;

import io.micronaut.starter.build.dependencies.CoordinateResolver;
import jakarta.inject.Singleton;

import java.util.Collections;
import java.util.List;

@Singleton
class MicronautGraalpyPygal extends MicronautGraalpy {

    MicronautGraalpyPygal(CoordinateResolver coordinateResolver) {
        super(coordinateResolver);
    }

    @Override
    public String getName() {
        return "graalpy-pygal";
    }

    @Override
    protected List<String> pythonPackages() {
        return Collections.singletonList("pygal==3.0.4");
    }
}
