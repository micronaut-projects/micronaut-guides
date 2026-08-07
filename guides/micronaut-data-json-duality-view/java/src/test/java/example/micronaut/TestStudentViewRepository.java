package example.micronaut;

import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;
import example.micronaut.domain.StudentView;
import example.micronaut.domain.StudentScheduleClassSubView;
import example.micronaut.domain.StudentScheduleSubView;
import example.micronaut.domain.Class;
import java.util.Optional;

import java.time.LocalTime;
import java.util.List;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@MicronautTest
class TestStudentViewRepository {

    @Inject
    StudentViewRepository studentViewRepository;

    @Inject
    ClassRepository classRepository;

    @Test
    void testCreateStudentView() {
        Class mathClass = classRepository.save(new Class(null, "Math"));
        StudentView studentView = new StudentView(null, "John", new ArrayList<>());
        StudentScheduleClassSubView studentScheduleClassSubView = new StudentScheduleClassSubView(null, mathClass.name());
        StudentScheduleSubView studentScheduleSubView = new StudentScheduleSubView(null, studentScheduleClassSubView);
        studentViewRepository.save(studentView);
        Optional<StudentView> student = studentViewRepository.findByName(studentView.name());
        assertNotNull(student);
    }
}