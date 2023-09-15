package example.micronaut;

import io.micronaut.data.annotation.Query;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.jpa.repository.JpaRepository;

import java.math.BigDecimal;
import java.util.Optional;

@Repository // <1>
public interface ProductRepository extends JpaRepository<Product, Long> { // <2>
  Optional<Product> findByCode(String code);

  @Query("update Product p set p.price = :price where p.code = :productCode") // <3>
  void updateProductPrice(String productCode, BigDecimal price);
}
