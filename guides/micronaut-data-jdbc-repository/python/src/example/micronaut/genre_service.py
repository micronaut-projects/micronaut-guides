from jakarta.inject import Singleton
from jakarta.transaction import Transactional
from micronaut.data.exceptions import DataAccessException

from .domain.genre import Genre
from .genre_repository import GenreRepository


@Singleton  # <1>
class GenreService:
    def __init__(self, genre_repository: GenreRepository):
        self.genre_repository = genre_repository

    @Transactional  # <2>
    def save_with_exception(self, genre: Genre) -> Genre:
        self.genre_repository.save(genre)
        raise DataAccessException("test exception")  # <3>
