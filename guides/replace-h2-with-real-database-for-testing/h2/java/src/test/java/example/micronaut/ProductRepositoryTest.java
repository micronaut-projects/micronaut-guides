package example.micronaut;

import io.micronaut.core.io.ResourceLoader;
import io.micronaut.test.annotation.Sql;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@MicronautTest
@Sql(scripts = "classpath:sql/seed-data.sql", phase = Sql.Phase.BEFORE_EACH) // <1>
class ProductRepositoryTest {

    @Inject
    Connection connection;

    @Inject
    ResourceLoader resourceLoader;

    @Test
    void shouldGetAllProducts(ProductRepository productRepository) {
        List<Product> products = productRepository.findAll();
        assertEquals(2, products.size());
    }
}
