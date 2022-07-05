package com.bloidonia;

import org.reactivestreams.Publisher;
import reactor.core.publisher.Mono;

import javax.validation.constraints.NotBlank;

public interface GenreRepository {

    Publisher<Genre> findAll();
    Mono<Genre> create(@NotBlank String name);

}
