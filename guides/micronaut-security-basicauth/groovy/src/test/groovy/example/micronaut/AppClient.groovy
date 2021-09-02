package example.micronaut

import groovy.transform.CompileStatic
import io.micronaut.http.annotation.Consumes
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.Header
import io.micronaut.http.client.annotation.Client

import static io.micronaut.http.MediaType.TEXT_PLAIN

@CompileStatic
@Client("/")
interface AppClient {

    @Consumes(TEXT_PLAIN) // <1>
    @Get
    String home(@Header String authorization) // <2>
}
