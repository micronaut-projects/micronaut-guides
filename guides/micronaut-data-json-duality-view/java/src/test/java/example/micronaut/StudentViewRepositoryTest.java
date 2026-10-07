package example.micronaut;

import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;
import example.micronaut.domain.StudentScheduleClassSubView;
import example.micronaut.domain.StudentScheduleSubView;
import example.micronaut.domain.StudentView;
import java.util.Optional;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@MicronautTest
class StudentViewRepositoryTest {

    @Inject
    StudentViewRepository studentViewRepository;

    @Inject
    ClassRepository classRepository;

    @Test
    void testCreateStudentView() {
        example.micronaut.domain.Class mathClass = classRepository.save(new example.micronaut.domain.Class(null, "Math"));
        StudentScheduleClassSubView studentScheduleClassSubView = new StudentScheduleClassSubView(mathClass.id(), mathClass.name());
        StudentScheduleSubView studentScheduleSubView = new StudentScheduleSubView(null, studentScheduleClassSubView);
        StudentView studentView = new StudentView(null, "John", List.of(studentScheduleSubView), Map.of("department", "Research", "remote", true));
        studentViewRepository.save(studentView);
        Optional<StudentView> student = studentViewRepository.findByName(studentView.name());
        assertTrue(student.isPresent());
        StudentView savedStudent = student.orElseThrow();
        assertEquals("Research", savedStudent.extras().get("department"));
        assertEquals(true, savedStudent.extras().get("remote"));
        assertEquals(1, savedStudent.classes().size());
        assertEquals("Math", savedStudent.classes().get(0).clazz().name());
    }
}
