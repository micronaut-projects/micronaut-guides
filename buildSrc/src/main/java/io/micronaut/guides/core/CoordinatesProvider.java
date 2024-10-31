package io.micronaut.guides.core;

import io.micronaut.starter.build.dependencies.Coordinate;

import java.util.Map;

public interface CoordinatesProvider {
    Map<String, Coordinate> getCoordinates();
}
