package example.micronaut

import io.micronaut.context.annotation.Requires
import io.micronaut.http.annotation.Consumes
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.Header
import io.micronaut.http.client.annotation.Client
import reactor.core.publisher.Mono

import static io.micronaut.context.env.Environment.TEST
import static io.micronaut.http.MediaType.TEXT_PLAIN

@Client(id = 'userecho') // <1>
@Requires(notEnv = TEST) // <2>
interface UserEchoClient extends UsernameFetcher {

    @Override
    @Consumes(TEXT_PLAIN)
    @Get('/user') // <3>
    Mono<String> findUsername(@Header('Authorization') String authorization) // <4>
}
