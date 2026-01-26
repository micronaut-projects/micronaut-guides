package example.micronaut;

import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

@MicronautTest
class TestStudentViewRepository {

    @Inject
    StudentViewRepository studentViewRepository;

    @Test
    void testCreateStudentView() {
        studentViewRepository.findAll();
    }
}