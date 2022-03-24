package example.micronaut;

import example.micronaut.domain.Student;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.PathVariable;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.annotation.Put;

import java.util.Optional;

@Controller("/student")
public class StudentController {

    private final StudentRepository studentRepository;

    public StudentController(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    @Get
    Iterable<Student> list() {
        return studentRepository.findAll();
    }

    @Post
    Student create(Student student) {
        return studentRepository.save(student);
    }

    @Get("/{id}")
    Optional<Student> find(@PathVariable String id) {
        return studentRepository.findById(id);
    }

    @Put
    Student update(Student student) {
        return studentRepository.update(student);
    }
}
