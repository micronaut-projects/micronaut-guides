package example.micronaut

import io.micronaut.core.annotation.NonNull
import groovy.transform.CompileStatic

import jakarta.inject.Singleton
import jakarta.validation.constraints.NotBlank

@CompileStatic
@Singleton // <1>
class UserFetcherService implements UserFetcher {

    private final UserJdbcRepository userGormService

    UserFetcherService(UserJdbcRepository userGormService) { // <2>
        this.userGormService = userGormService
    }

    @Override
    Optional<UserState> findByUsername(@NotBlank @NonNull String username) {
        userGormService.findByUsername(username).map(it -> it as UserState)
    }
}
