package example.micronaut;

import io.reactivex.Single;

public interface UsernameFetcher {
    Single<String> findUsername();
}
