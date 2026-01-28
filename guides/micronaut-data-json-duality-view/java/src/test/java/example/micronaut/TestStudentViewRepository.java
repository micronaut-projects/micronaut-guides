package example.micronaut;

import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;
import example.micronaut.domain.StudentView;
import example.micronaut.domain.StudentScheduleClassSubView;
import example.micronaut.domain.StudentScheduleSubView;
import example.micronaut.domain.Class;

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
        Class mathClass = classRepository.save(new Class("Math"));

        StudentView studentView = new StudentView();
        studentView.setName("John");

        StudentScheduleClassSubView studentScheduleClassSubView = new StudentScheduleClassSubView();

        StudentScheduleSubView studentScheduleSubView = new StudentScheduleSubView(studentScheduleClassSubView);
        studentScheduleClassSubView.setClassID(mathClass.getId());

        List<StudentScheduleSubView> studentClasses = new ArrayList<>();
        studentClasses.add(studentScheduleSubView);
        studentView.setClasses(studentClasses);

        studentViewRepository.save(studentView);
        assertNotNull(studentViewRepository.findById(studentView.getId()));
    }
}