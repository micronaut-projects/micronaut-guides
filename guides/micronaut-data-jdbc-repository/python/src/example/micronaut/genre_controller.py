from typing import Annotated

import java
from jakarta.inject import Inject
from jakarta.validation import Valid
from micronaut.data.exceptions import DataAccessException
from micronaut.http import HttpHeaders, HttpResponse, HttpStatus
from micronaut.http.annotation import Body, Delete, Get, Post, Put, Status
from micronaut.scheduling import TaskExecutors
from micronaut.scheduling.annotation import ExecuteOn

from .domain.genre import Genre
from .genre_repository import GenreRepository
from .genre_service import GenreService
from .genre_update_command import GenreUpdateCommand

Pageable = java.type("io.micronaut.data.model.Pageable")

genre_repository: Annotated[GenreRepository, Inject]  # <3>
genre_service: Annotated[GenreService, Inject]


@ExecuteOn(TaskExecutors.BLOCKING)  # <1>
@Get("/genres/{id}")  # <2> <4>
def show(id: int) -> Genre | None:
    return genre_repository.findById(id).orElse(None)  # <5>


@ExecuteOn(TaskExecutors.BLOCKING)
@Put("/genres")  # <6>
def update(command: Annotated[GenreUpdateCommand, Body, Valid]) -> HttpResponse:  # <7>
    genre_repository.update(Genre(command.id, command.name))
    return (
        HttpResponse.noContent()
        .header(HttpHeaders.LOCATION, location(command.id))  # <8>
    )


@ExecuteOn(TaskExecutors.BLOCKING)
@Get("/genres/list")  # <9>
def list(pageable: Annotated[Pageable, Valid]) -> list[Genre]:  # <10>
    return genre_repository.findAll(pageable)


@ExecuteOn(TaskExecutors.BLOCKING)
@Post("/genres")  # <11>
def save(genre: Annotated[Genre, Body, Valid]) -> HttpResponse[Genre]:
    saved = genre_repository.save(genre)
    return (
        HttpResponse.created(saved)
        .header(HttpHeaders.LOCATION, location(saved.id))
    )


@ExecuteOn(TaskExecutors.BLOCKING)
@Post("/genres/ex")  # <12>
def save_exceptions(genre: Annotated[Genre, Body, Valid]) -> HttpResponse[Genre]:
    try:
        saved = genre_service.save_with_exception(genre)
        return (
            HttpResponse.created(saved)
            .header(HttpHeaders.LOCATION, location(saved.id))
        )
    except DataAccessException:
        return HttpResponse.noContent()


@ExecuteOn(TaskExecutors.BLOCKING)
@Delete("/genres/{id}")  # <13>
@Status(HttpStatus.NO_CONTENT)
def delete(id: int) -> None:
    genre_repository.deleteById(id)


def location(id: int) -> str:
    return f"/genres/{id}"
