package example.micronaut;

import example.micronaut.domain.Course;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.data.annotation.Join;
import io.micronaut.data.mongodb.annotation.MongoRepository;
import io.micronaut.data.repository.CrudRepository;

import java.util.Optional;

@MongoRepository
public interface CourseRepository extends CrudRepository<Course, String> {

    @Override
    @NonNull
    @Join(value = "students")
    Optional<Course> findById(@NonNull String id);
}
