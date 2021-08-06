package example.micronaut

import groovy.transform.CompileStatic

import javax.inject.Singleton

@CompileStatic
@Singleton // <1>
class AuthoritiesFetcherService implements AuthoritiesFetcher {

    private final UserRoleGormService userRoleGormService

    AuthoritiesFetcherService(UserRoleGormService userRoleGormService) { // <2>
        this.userRoleGormService = userRoleGormService
    }

    @Override
    List<String> findAuthoritiesByUsername(String username) {
        userRoleGormService.findAllAuthoritiesByUsername(username)
    }
}
