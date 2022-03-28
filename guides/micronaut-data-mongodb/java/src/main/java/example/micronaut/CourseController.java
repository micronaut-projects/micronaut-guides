package example.micronaut;

import example.micronaut.domain.Course;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.PathVariable;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.annotation.Put;

import java.util.Optional;

@Controller("/course") // <1>
public class CourseController {

    private final RegistrationService registrationService;

    public CourseController(RegistrationService registrationService) { // <2>
        this.registrationService = registrationService;
    }

    @Get // <3>
    Iterable<Course> list() {
        return registrationService.findAllCourses();
    }

    @Post
    Course create(String name) {
        return registrationService.createOrUpdateCourse(new Course(name));
    }

    @Get("/{id}")
    Optional<Course> get(@PathVariable String id) {
        return registrationService.findCourseById(id);
    }

    @Put
    Course update(Course course) {
        return registrationService.createOrUpdateCourse(course);
    }
}
