from java.math import BigDecimal
from java.util import Optional
from micronaut.data.jdbc.annotation import JdbcRepository
from micronaut.data.model.query.builder.sql import Dialect
from micronaut.data.repository import CrudRepository

from .product import Product


@JdbcRepository(dialect=Dialect.MYSQL)  # <1>
class ProductRepository(CrudRepository[Product, int]):  # <2>
    def findByCode(self, code: str) -> Optional[Product]: ...

    def updateByCode(self, code: str, price: BigDecimal) -> int: ...  # <3>
