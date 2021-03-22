package example.micronaut;

import io.micronaut.context.annotation.Requires;
import io.micronaut.context.env.Environment;
import io.micronaut.http.annotation.Header;
import io.reactivex.Single;

import javax.inject.Singleton;

@Requires(env = Environment.TEST)
@Singleton
public class UserEchoClientReplacement implements UsernameFetcher {

    @Override
    public Single<String> findUsername(@Header("Authorization") String authorization) {
        return Single.just("sherlock");
    }
}
