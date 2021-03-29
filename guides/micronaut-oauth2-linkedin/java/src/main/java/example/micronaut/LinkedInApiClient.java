package example.micronaut;

import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Header;
import io.micronaut.http.client.annotation.Client;
import io.reactivex.Flowable;

@Client(id = "linkedin") // <1>
public interface LinkedInApiClient {

    @Get("/v2/me") // <2>
    Flowable<LinkedInMe> me(@Header String authorization); // <3>
}
