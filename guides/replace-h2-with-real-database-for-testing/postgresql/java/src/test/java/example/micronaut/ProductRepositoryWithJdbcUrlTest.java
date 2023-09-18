package example.micronaut;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import io.micronaut.context.annotation.Property;
import io.micronaut.core.io.ResourceLoader;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

@MicronautTest(startApplication = false) // <1>
@Property(name = "datasources.default.driver-class-name",
        value = "org.testcontainers.jdbc.ContainerDatabaseDriver") // <2>
@Property(name = "datasources.default.url",
        value = "jdbc:tc:postgresql:15.2-alpine:///db?TC_INITSCRIPT=sql/init-db.sql") // <3>
class ProductRepositoryWithJdbcUrlTest {
    @Inject
    Connection connection;

    @Inject
    ResourceLoader resourceLoader;

    @BeforeEach
    void setUp() throws IOException, SQLException {
        SqlUtils.load(connection, resourceLoader, "sql/seed-data.sql");
    }

    @Test
    void shouldGetAllProducts(ProductRepository productRepository) {
        List<Product> products = productRepository.findAll();
        assertEquals(2, products.size());
    }
}
