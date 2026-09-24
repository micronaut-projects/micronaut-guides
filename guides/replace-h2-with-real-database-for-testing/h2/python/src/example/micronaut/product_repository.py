from micronaut.data.annotation import Query
from micronaut.data.jdbc.annotation import JdbcRepository
from micronaut.data.model.query.builder.sql import Dialect
from micronaut.data.repository import CrudRepository

from .product import Product


# tag::clazz[]
@JdbcRepository(dialect=Dialect.H2)  # <1>
class ProductRepository(CrudRepository[Product, int]):  # <2>
    # end::clazz[]
    # tag::methods[]
    def createProductIfNotExists(self, product: Product):
        return self.createProductIfNotExistsById(product.id, product.code, product.name)

    @Query(
        value="insert into products(id, code, name) values(:id, :code, :name) ON CONFLICT DO NOTHING",
        nativeQuery=True,
    )  # <3>
    def createProductIfNotExistsById(self, id: int, code: str, name: str) -> None: ...
    # end::methods[]
    # tag::close[]
    pass
    # end::close[]
