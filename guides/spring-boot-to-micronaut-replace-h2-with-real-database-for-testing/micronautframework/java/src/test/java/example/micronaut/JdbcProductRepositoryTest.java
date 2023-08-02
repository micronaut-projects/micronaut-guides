package example.micronaut;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.net.URL;
import java.util.List;
import java.util.Optional;

import io.micronaut.context.annotation.Property;
import io.micronaut.core.io.ResourceLoader;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.JdbcTemplate;

@MicronautTest
@Property(name = "datasources.default.driver-class-name", value = "org.testcontainers.jdbc.ContainerDatabaseDriver")
@Property(name = "datasources.default.url", value = "jdbc:tc:postgresql:15.2-alpine:///db?TC_INITSCRIPT=sql/init-db.sql")
class JdbcProductRepositoryTest {

  @Inject
  private JdbcTemplate jdbcTemplate;

  @Inject
  ResourceLoader resourceLoader;

  @Inject
  JdbcProductRepository productRepository;

  @BeforeEach
  void setUp() {
    //Optional<URL> resource = resourceLoader.getResource("sql/seed-data.sql");
    String foo = "";
  }

  @Test
  //@Sql("/sql/seed-data.sql")
  void shouldGetAllProducts() {
    List<Product> products = productRepository.getAllProducts();
    assertEquals(2, products.size());
  }
}
