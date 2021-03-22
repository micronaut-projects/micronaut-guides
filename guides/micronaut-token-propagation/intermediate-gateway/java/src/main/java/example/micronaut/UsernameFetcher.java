package example.micronaut;

import io.micronaut.http.annotation.Header;
import io.reactivex.Single;

public interface UsernameFetcher {
    Single<String> findUsername(@Header("Authorization") String authorization);
}
