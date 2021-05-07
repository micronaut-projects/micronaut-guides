package example.micronaut

import edu.umd.cs.findbugs.annotations.NonNull
import groovy.transform.CompileStatic

import javax.inject.Singleton
import javax.validation.constraints.NotBlank

@CompileStatic
@Singleton // <1>
class UserFetcherService implements UserFetcher {

    protected final UserGormService userGormService

    UserFetcherService(UserGormService userGormService) { // <2>
        this.userGormService = userGormService
    }

    @Override
    UserState findByUsername(@NotBlank @NonNull String username) {
        userGormService.findByUsername(username) as UserState
    }
}
