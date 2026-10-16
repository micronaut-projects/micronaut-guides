from typing import Annotated

import java
from jakarta.inject import Inject
from jakarta.validation import Valid
from jakarta.validation.constraints import NotBlank
from micronaut.core.async_.annotation import SingleResult
from micronaut.http import HttpHeaders, HttpResponse
from micronaut.http.annotation import Body, Delete, Get, Post, Put
from org.reactivestreams import Publisher
from reactor.core.publisher import Mono

from .domain.genre import Genre
from .genre_repository import GenreRepository
from .genre_update_command import GenreUpdateCommand

Pageable = java.type("io.micronaut.data.model.Pageable")

genre_repository: Annotated[GenreRepository, Inject]  # <2>


@Get("/genres/{id}")  # <1> <3>
@SingleResult
def show(id: int) -> Publisher[Genre]:
    return genre_repository.findById(id)  # <4>


@Put("/genres")  # <5>
@SingleResult
def update(command: Annotated[GenreUpdateCommand, Body, Valid]) -> Publisher[HttpResponse]:  # <6>
    return Mono.from_(genre_repository.update(command.id, command.name)).thenReturn(
        HttpResponse.noContent()
        .header(HttpHeaders.LOCATION, location(command.id))  # <7>
    )


@Get("/genres/list")  # <8>
@SingleResult
def list(pageable: Annotated[Pageable, Valid]) -> Publisher[list[Genre]]:  # <9>
    return Mono.from_(genre_repository.findAll(pageable)).map(page_content)


def page_content(page):
    return page.getContent()


@Post("/genres")  # <10>
@SingleResult
def save(name: Annotated[str, Body("name"), NotBlank]) -> Publisher[HttpResponse]:
    return Mono.from_(genre_repository.save(name)).map(created_genre)


@Post("/genres/ex")  # <11>
@SingleResult
def save_exceptions(name: Annotated[str, Body("name"), NotBlank]) -> Publisher[HttpResponse]:
    return (
        Mono.from_(genre_repository.saveWithException(name))
        .map(created_genre)
        .onErrorReturn(HttpResponse.noContent())
    )


@Delete("/genres/{id}")  # <12>
@SingleResult
def delete(id: int) -> Publisher[HttpResponse]:
    return Mono.from_(genre_repository.deleteById(id)).thenReturn(HttpResponse.noContent())


def created_genre(genre: Genre) -> HttpResponse[Genre]:
    return (
        HttpResponse.created(genre)
        .header(HttpHeaders.LOCATION, location(genre.id))
    )


def location(id: int) -> str:
    return f"/genres/{id}"
