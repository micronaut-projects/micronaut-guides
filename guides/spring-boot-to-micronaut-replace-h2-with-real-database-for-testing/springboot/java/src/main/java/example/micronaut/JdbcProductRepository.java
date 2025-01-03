package example.micronaut;

import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class JdbcProductRepository {

  private final JdbcTemplate jdbcTemplate;

  public JdbcProductRepository(JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
  }

  public List<Product> getAllProducts() {
    return jdbcTemplate.query(
      "select id, code, name from products",
      (rs, rowNum) -> {
        long id = rs.getLong("id");
        String code = rs.getString("code");
        String name = rs.getString("name");
        return new Product(id, code, name);
      }
    );
  }
}
