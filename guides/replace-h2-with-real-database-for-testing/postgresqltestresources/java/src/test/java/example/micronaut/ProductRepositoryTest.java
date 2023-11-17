package example.micronaut;

import io.micronaut.core.io.ResourceLoader;
import io.micronaut.test.annotation.Sql;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@MicronautTest(startApplication = false) // <1>
@Sql(scripts = {"classpath:sql/init-db.sql", "classpath:sql/seed-data.sql"},
        phase = Sql.Phase.BEFORE_EACH) // <2>
class ProductRepositoryTest {

    @Inject
    Connection connection;

    @Inject
    ResourceLoader resourceLoader;

    @Inject
    ProductRepository productRepository;

    @Test
    void shouldGetAllProducts() {
        List<Product> products = productRepository.findAll();
        assertEquals(2, products.size());
    }

    @Test
    void shouldNotCreateAProductWithDuplicateCode() {
        Product product = new Product(3L, "p101", "Test Product");
        assertDoesNotThrow(() -> productRepository.createProductIfNotExists(product));
        Optional<Product> optionalProduct = productRepository.findById(product.getId());
        assertTrue(optionalProduct.isEmpty());
    }

}
