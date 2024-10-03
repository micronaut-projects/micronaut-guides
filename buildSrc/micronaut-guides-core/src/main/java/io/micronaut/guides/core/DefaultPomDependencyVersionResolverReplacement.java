package io.micronaut.guides.core;

import io.micronaut.context.annotation.Replaces;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.io.ResourceResolver;
import io.micronaut.starter.build.dependencies.Coordinate;
import io.micronaut.starter.build.dependencies.DefaultPomDependencyVersionResolver;
import io.micronaut.starter.build.dependencies.StarterCoordinates;
import jakarta.inject.Singleton;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static io.micronaut.starter.build.dependencies.CoordinatesUtils.readCoordinates;

@Replaces(DefaultPomDependencyVersionResolver.class)
@Singleton
public class DefaultPomDependencyVersionResolverReplacement extends DefaultPomDependencyVersionResolver {
    private final Map<String, Coordinate> coordinates;
    public DefaultPomDependencyVersionResolverReplacement(ResourceResolver resourceResolver) {
        Map<String, Coordinate> coordinateMap = new HashMap<>();
        coordinateMap.putAll(StarterCoordinates.ALL_COORDINATES);
        coordinateMap.putAll(readCoordinates(resourceResolver.getResources("classpath:pom.xml")));
        this.coordinates = coordinateMap;
    }

    @NonNull
    public Map<String, Coordinate> getCoordinates() {
        return coordinates;
    }

    @Override
    @NonNull
    public Optional<Coordinate> resolve(@NonNull String artifactId) {
        return Optional.ofNullable(coordinates.get(artifactId));
    }
}
