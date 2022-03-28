package example.micronaut;

import example.micronaut.domain.Course;
import example.micronaut.domain.Student;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.scheduling.annotation.ExecuteOn;
import jakarta.inject.Singleton;

import java.util.Optional;

import static io.micronaut.scheduling.TaskExecutors.IO;

@Singleton
@ExecuteOn(IO) // <1>
public class RegistrationService {

    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;

    public RegistrationService(StudentRepository studentRepository, CourseRepository courseRepository) {
        this.studentRepository = studentRepository;
        this.courseRepository = courseRepository;
    }

    Iterable<Student> findAllStudents() {
        return studentRepository.findAll();
    }

    Optional<Student> findStudentById(@NonNull String id) {
        return studentRepository.findById(id);
    }

    public Student createOrUpdateStudent(@NonNull Student student) {
        if (student.getId() == null) {
            return studentRepository.save(student);
        } else {
            return studentRepository.update(student);
        }
    }

    Iterable<Course> findAllCourses() {
        return courseRepository.findAll();
    }

    Optional<Course> findCourseById(@NonNull String id) {
        return courseRepository.findById(id);
    }

    public Course createOrUpdateCourse(@NonNull Course course) {
        if (course.getId() == null) {
            return courseRepository.save(course);
        } else {
            return courseRepository.update(course);
        }
    }
}
