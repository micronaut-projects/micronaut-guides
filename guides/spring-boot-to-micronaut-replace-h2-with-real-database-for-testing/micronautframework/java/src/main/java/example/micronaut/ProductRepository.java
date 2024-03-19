package example.micronaut;

import io.micronaut.context.annotation.Parameter;
import io.micronaut.data.annotation.Query;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;

@Repository
interface ProductRepository extends CrudRepository<Product, Long> {
  @Query(
    value = """
            insert into products(id, code, name) 
            values(:#{#p.id}, :#{#p.code}, :#{#p.name}) 
            ON CONFLICT DO NOTHING
            """,
    nativeQuery = true
  )
  void createProductIfNotExists(@Parameter("p") Product product);
}
