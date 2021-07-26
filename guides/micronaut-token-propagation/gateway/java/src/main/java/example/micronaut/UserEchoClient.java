package example.micronaut;

import io.micronaut.context.annotation.Requires;
import io.micronaut.context.env.Environment;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Consumes;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.client.annotation.Client;
import org.reactivestreams.Publisher;
import io.micronaut.core.async.annotation.SingleResult;

@Client(id = "userecho")
@Requires(notEnv = Environment.TEST)
public interface UserEchoClient extends UsernameFetcher {

    @Consumes(MediaType.TEXT_PLAIN)
    @Get("/user")
    @SingleResult
    Publisher<String> findUsername();
}
