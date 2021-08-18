package example.micronaut

import io.micronaut.core.annotation.NonNull
import groovy.transform.CompileStatic

import jakarta.inject.Singleton
import javax.validation.constraints.NotBlank

@CompileStatic
@Singleton // <1>
class UserFetcherService implements UserFetcher {

    private final UserGormService userGormService

    UserFetcherService(UserGormService userGormService) { // <2>
        this.userGormService = userGormService
    }

    @Override
    UserState findByUsername(@NotBlank @NonNull String username) {
        userGormService.findByUsername(username) as UserState
    }
}
