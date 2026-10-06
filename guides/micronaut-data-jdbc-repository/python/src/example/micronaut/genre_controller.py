from typing import Annotated

from jakarta.validation import Valid
from micronaut.data.exceptions import DataAccessException
from micronaut.data.model import Pageable
from micronaut.http import HttpHeaders, HttpResponse, HttpStatus
from micronaut.http.annotation import Body, Controller, Delete, Get, Post, Put, Status
from micronaut.scheduling import TaskExecutors
from micronaut.scheduling.annotation import ExecuteOn

from .domain.genre import Genre
from .genre_repository import GenreRepository
from .genre_service import GenreService
from .genre_update_command import GenreUpdateCommand

@ExecuteOn(TaskExecutors.BLOCKING)  # <1>
@Controller("/genres")  # <2>
class GenreController:
    def __init__(self, genre_repository: GenreRepository, genre_service: GenreService):  # <3>
        self.genre_repository = genre_repository
        self.genre_service = genre_service

    @Get("/{id}")  # <4>
    def show(self, id: int) -> Genre | None:
        return self.genre_repository.getById(id)  # <5>

    @Put  # <6>
    def update(self, command: Annotated[GenreUpdateCommand, Body, Valid]) -> HttpResponse:  # <7>
        self.genre_repository.update(Genre(command.id, command.name))
        return (
            HttpResponse.noContent()
            .header(HttpHeaders.LOCATION, location(command.id))  # <8>
        )

    @Get("/list")  # <9>
    def list_genres(self, pageable: Annotated[Pageable, Valid]) -> list[Genre]:  # <10>
        return self.genre_repository.findAll(pageable)

    @Post  # <11>
    def save(self, genre: Annotated[Genre, Body, Valid]) -> HttpResponse[Genre]:
        saved = self.genre_repository.save(genre)
        return (
            HttpResponse.created(saved)
            .header(HttpHeaders.LOCATION, location(saved.id))
        )

    @Post("/ex")  # <12>
    def save_exceptions(self, genre: Annotated[Genre, Body, Valid]) -> HttpResponse[Genre]:
        try:
            saved = self.genre_service.save_with_exception(genre)
            return (
                HttpResponse.created(saved)
                .header(HttpHeaders.LOCATION, location(saved.id))
            )
        except DataAccessException:
            return HttpResponse.noContent()

    @Delete("/{id}")  # <13>
    @Status(HttpStatus.NO_CONTENT)
    def delete(self, id: int) -> None:
        self.genre_repository.deleteById(id)


def location(id: int) -> str:
    return f"/genres/{id}"
