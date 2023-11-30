package example.micronaut;

import example.micronaut.repositories.UserJdbcRepository;
import io.micronaut.core.annotation.NonNull;
import jakarta.inject.Singleton;
import jakarta.validation.constraints.NotBlank;

import java.util.Optional;

@Singleton // <1>
class UserFetcherService implements UserFetcher {

    private final UserJdbcRepository userJdbcRepository;

    UserFetcherService(UserJdbcRepository userJdbcRepository) { // <2>
        this.userJdbcRepository = userJdbcRepository;
    }

    @Override
    public Optional<UserState> findByUsername(@NotBlank @NonNull String username) {
        return userJdbcRepository.findByUsername(username).map(UserState.class::cast);
    }
}
