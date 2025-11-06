package example.micronaut;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import io.micronaut.context.annotation.Property;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

@MicronautTest
@Property(name = "spec.name", value = "jdbc:tc:postgresql:15.2-alpine:///db?TC_INITSCRIPT=sql/init-db.sql")
class ProductRepositoryWithJdbcUrlTest {

  @Inject
  ProductRepository productRepository;

  @Test
  //@Sql("classpath:/sql/seed-data.sql")
  void shouldGetAllProducts() {
    List<Product> products = productRepository.findAll();
    assertEquals(2, products.size());
  }
}
