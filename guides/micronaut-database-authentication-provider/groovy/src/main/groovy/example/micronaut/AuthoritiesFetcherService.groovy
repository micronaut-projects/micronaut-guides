package example.micronaut

import javax.inject.Singleton

@Singleton // <1>
class AuthoritiesFetcherService implements AuthoritiesFetcher {

    protected final UserRoleGormService userRoleGormService

    AuthoritiesFetcherService(UserRoleGormService userRoleGormService) {  // <2>
        this.userRoleGormService = userRoleGormService
    }

    @Override
    List<String> findAuthoritiesByUsername(String username) {
        userRoleGormService.findAllAuthoritiesByUsername(username)
    }
}
