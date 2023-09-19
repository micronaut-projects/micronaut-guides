package example.micronaut;

import io.micronaut.data.annotation.Query;
import io.micronaut.data.jdbc.annotation.JdbcRepository;
import io.micronaut.data.model.query.builder.sql.Dialect;
import io.micronaut.data.repository.CrudRepository;

@JdbcRepository(dialect = Dialect.POSTGRES) // <1>
interface ProductRepository extends CrudRepository<Product, Long> { // <2>

    default void createProductIfNotExists(Product product) {
        createProductIfNotExists(product.getId(), product.getCode(), product.getName());
    }

    @Query(
            value = "insert into products(id, code, name) values(:id, :code, :name) ON CONFLICT DO NOTHING",
            nativeQuery = true
    ) // <3>
    void createProductIfNotExists(Long id, String code, String name);
}
