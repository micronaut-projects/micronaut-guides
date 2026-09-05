package example.micronaut;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import io.micronaut.test.support.TestPropertyProvider;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.MountableFile;

@MicronautTest
@Testcontainers(disabledWithoutDocker = true)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ProductRepositoryTest implements TestPropertyProvider {

  @Container
  static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>(
    "postgres:15.2-alpine"
  )
    .withCopyFileToContainer(
      MountableFile.forClasspathResource("sql/init-db.sql"),
      "/docker-entrypoint-initdb.d/init-db.sql"
    );

  @Override
  public @NonNull Map<String, String> getProperties() {
    return Map.of("spring.datasource.url", postgres.getJdbcUrl(),
            "spring.datasource.username", postgres.getUsername(),
            "spring.datasource.password", postgres.getPassword());
  }

  @Inject
  ProductRepository productRepository;

  @Test
  //@Sql("/sql/seed-data.sql")
  void shouldGetAllProducts() {
    List<Product> products = productRepository.findAll();
    assertEquals(2, products.size());
  }

  @Test
  //@Sql("/sql/seed-data.sql")
  void shouldNotCreateAProductWithDuplicateCode() {
    Product product = new Product(3L, "p101", "Test Product");
    productRepository.createProductIfNotExists(product);
    Optional<Product> optionalProduct = productRepository.findById(product.getId());
    assertThat(optionalProduct).isEmpty();
  }

}
