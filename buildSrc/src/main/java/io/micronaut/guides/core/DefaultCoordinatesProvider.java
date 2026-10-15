package io.micronaut.guides.core;

import io.micronaut.context.ApplicationContext;
import io.micronaut.starter.build.dependencies.Coordinate;
import io.micronaut.starter.build.dependencies.PomDependencyVersionResolver;
import jakarta.inject.Singleton;

import java.util.Map;

@Singleton
public class DefaultCoordinatesProvider implements CoordinatesProvider {
    @Override
    public Map<String, Coordinate> getCoordinates() {
        try (ApplicationContext context = ApplicationContext.run()) {
            PomDependencyVersionResolver pomDependencyVersionResolver = context.getBean(PomDependencyVersionResolver.class);
            return pomDependencyVersionResolver.getCoordinates();
        }
    }
}
