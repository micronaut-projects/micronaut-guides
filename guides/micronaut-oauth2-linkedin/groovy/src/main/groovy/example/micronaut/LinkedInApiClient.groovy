package example.micronaut

import groovy.transform.CompileStatic
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.Header
import io.micronaut.http.client.annotation.Client
import org.reactivestreams.Publisher

@CompileStatic
@Client(id = 'linkedin') // <1>
interface LinkedInApiClient {

    @Get('/v2/me') // <2>
    Publisher<LinkedInMe> me(@Header String authorization) // <3> <4>
}
