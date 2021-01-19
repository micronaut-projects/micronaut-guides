package example.micronaut

import groovy.transform.CompileStatic
import io.micronaut.http.MediaType
import io.micronaut.http.annotation.Consumes
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.Header
import io.micronaut.http.client.annotation.Client

@CompileStatic
@Client("/")
interface AppClient {

    @Consumes(MediaType.TEXT_PLAIN)
    @Get("/")
    String home(@Header String authorization)
}
