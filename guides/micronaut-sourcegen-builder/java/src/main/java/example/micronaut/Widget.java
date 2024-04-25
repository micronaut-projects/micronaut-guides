package example.micronaut;

import io.micronaut.sourcegen.annotations.Builder;

@Builder
public record Widget(String name, int id) {
}