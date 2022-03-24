package example.micronaut.clients

import example.micronaut.domain.Student
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.PathVariable
import io.micronaut.http.annotation.Post
import io.micronaut.http.annotation.Put
import io.micronaut.http.client.annotation.Client

@Client("/student")
interface StudentClient {

    @Get
    Iterable<Student> list()

    @Post
    Student create(Student student)

    @Get("/{id}")
    Optional<Student> find(@PathVariable String id)

    @Put
    Student update(Student student)
}