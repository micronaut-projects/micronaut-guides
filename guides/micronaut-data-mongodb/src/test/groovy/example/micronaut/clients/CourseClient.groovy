package example.micronaut.clients

import example.micronaut.domain.Course
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.PathVariable
import io.micronaut.http.annotation.Post
import io.micronaut.http.annotation.Put
import io.micronaut.http.client.annotation.Client

@Client("/course")
interface CourseClient {

    @Get
    Iterable<Course> list()

    @Post
    Course create(String name)

    @Get("/{id}")
    Optional<Course> get(@PathVariable String id)

    @Put
    Course update(Course course)
}
