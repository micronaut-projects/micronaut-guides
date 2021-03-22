package example.micronaut;

import io.micronaut.context.annotation.Requires;
import io.micronaut.context.env.Environment;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Consumes;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.client.annotation.Client;
import io.reactivex.Single;

@Client(id = "userecho")
@Requires(notEnv = Environment.TEST)
public interface UserEchoClient extends UsernameFetcher {

    @Consumes(MediaType.TEXT_PLAIN)
    @Get("/user")
    Single<String> findUsername();
}
