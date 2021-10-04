package example.micronaut

import io.micronaut.http.MediaType.TEXT_PLAIN
import io.micronaut.http.annotation.Consumes
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.Header
import io.micronaut.http.client.annotation.Client

@Client("/")
interface AppClient {

    @Consumes(TEXT_PLAIN) // <1>
    @Get
    fun home(@Header authorization: String): String // <2>
}
