package example.micronaut;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

interface ProductRepository extends JpaRepository<Product, Long> {
  @Modifying
  @Query(
    value = """
            insert into products(id, code, name) 
            values(:#{#p.id}, :#{#p.code}, :#{#p.name}) 
            ON CONFLICT DO NOTHING
            """,
    nativeQuery = true
  )
  void createProductIfNotExists(@Param("p") Product product);
}
