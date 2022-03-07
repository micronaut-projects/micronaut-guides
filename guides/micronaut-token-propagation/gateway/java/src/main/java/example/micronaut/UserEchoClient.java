package example.micronaut;

import io.micronaut.context.annotation.Requires;
import io.micronaut.http.annotation.Consumes;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.client.annotation.Client;
import reactor.core.publisher.Mono;

import static io.micronaut.context.env.Environment.TEST;
import static io.micronaut.http.MediaType.TEXT_PLAIN;

@Client(id = "userecho")
@Requires(notEnv = TEST)
public interface UserEchoClient extends UsernameFetcher {

    @Consumes(TEXT_PLAIN)
    @Get("/user")
    Mono<String> findUsername();
}
