package io.micronaut.guides;

import io.micronaut.context.annotation.Replaces;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.io.ResourceResolver;
import io.micronaut.starter.build.dependencies.Coordinate;
import io.micronaut.starter.build.dependencies.DefaultPomDependencyVersionResolver;
import io.micronaut.starter.build.dependencies.StarterCoordinates;
import jakarta.inject.Singleton;

import java.util.Map;
import java.util.Optional;

import static io.micronaut.starter.build.dependencies.CoordinatesUtils.readCoordinates;

@Replaces(DefaultPomDependencyVersionResolver.class)
@Singleton
public class DefaultPomDependencyVersionResolverReplacement extends DefaultPomDependencyVersionResolver {
    private final Map<String, Coordinate> coordinates;
    public DefaultPomDependencyVersionResolverReplacement(ResourceResolver resourceResolver) {
        coordinates = readCoordinates(resourceResolver.getResources("classpath:pom.xml"));
    }

    @Override
    @NonNull
    public Optional<Coordinate> resolve(@NonNull String artifactId) {
        return Optional.ofNullable(coordinates.computeIfAbsent(artifactId, k -> StarterCoordinates.ALL_COORDINATES.get(artifactId)));
    }
}
