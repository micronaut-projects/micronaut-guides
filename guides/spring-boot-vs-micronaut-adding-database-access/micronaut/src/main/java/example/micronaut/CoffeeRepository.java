package example.micronaut;

import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;

@Repository
public interface CoffeeRepository extends CrudRepository<Coffee, String> {
}
