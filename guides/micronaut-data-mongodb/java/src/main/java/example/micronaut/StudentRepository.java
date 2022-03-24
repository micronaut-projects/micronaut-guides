package example.micronaut;

import example.micronaut.domain.Student;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.data.annotation.Join;
import io.micronaut.data.mongodb.annotation.MongoRepository;
import io.micronaut.data.repository.CrudRepository;

import java.util.Optional;

@MongoRepository
public interface StudentRepository extends CrudRepository<Student, String> {

    @Override
    @NonNull
    @Join(value = "courses")
    Optional<Student> findById(@NonNull String id);
}
