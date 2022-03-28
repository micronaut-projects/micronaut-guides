package example.micronaut;

import example.micronaut.domain.Student;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.PathVariable;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.annotation.Put;

import java.util.Optional;

@Controller("/student") // <1>
public class StudentController {

    private final RegistrationService registrationService;

    public StudentController(RegistrationService registrationService) { // <2>
        this.registrationService = registrationService;
    }

    @Get
    Iterable<Student> list() {
        return registrationService.findAllStudents();
    }

    @Post
    Student create(Student student) {
        return registrationService.createOrUpdateStudent(student);
    }

    @Get("/{id}")
    Optional<Student> find(@PathVariable String id) {
        return registrationService.findStudentById(id);
    }

    @Put
    Student update(Student student) {
        return registrationService.createOrUpdateStudent(student);
    }
}
