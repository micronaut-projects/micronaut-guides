package example.micronaut;

import io.micronaut.context.annotation.Requires;
import io.micronaut.context.env.Environment;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Consumes;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Header;
import io.micronaut.http.client.annotation.Client;
import org.reactivestreams.Publisher;
import io.micronaut.core.async.annotation.SingleResult;

@Client(id = "userecho") // <1>
@Requires(notEnv = Environment.TEST) // <2>
public interface UserEchoClient extends UsernameFetcher {

    @Override
    @Consumes(MediaType.TEXT_PLAIN)
    @Get("/user") // <3>
    @SingleResult
    Publisher<String> findUsername(@Header("Authorization") String authorization); // <4>
}
