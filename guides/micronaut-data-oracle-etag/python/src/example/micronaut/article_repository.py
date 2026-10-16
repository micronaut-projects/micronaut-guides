from micronaut.data.jdbc.annotation import JdbcRepository
from micronaut.data.repository import CrudRepository

from .article import Article


@JdbcRepository(dialect="ORACLE")
class ArticleRepository(CrudRepository[Article, int]):
    def getById(self, id: int) -> Article | None: ...
