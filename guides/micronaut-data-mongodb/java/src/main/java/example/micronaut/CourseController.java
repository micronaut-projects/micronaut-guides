package example.micronaut;

import example.micronaut.domain.Course;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.PathVariable;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.annotation.Put;

import java.util.Optional;

@Controller("/course")
public class CourseController {

    private final CourseRepository courseRepository;

    public CourseController(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    @Get
    Iterable<Course> list() {
        return courseRepository.findAll();
    }

    @Post
    Course create(String name) {
        return courseRepository.save(new Course(name));
    }

    @Get("/{id}")
    Optional<Course> get(@PathVariable String id) {
        return courseRepository.findById(id);
    }

    @Put
    Course update(Course course) {
        return courseRepository.update(course);
    }
}
