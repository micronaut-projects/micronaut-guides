package example.micronaut;

import example.micronaut.repositories.UserRoleJdbcRepository;
import jakarta.inject.Singleton;

import java.util.List;

@Singleton // <1>
class AuthoritiesFetcherService implements AuthoritiesFetcher {

    private final UserRoleJdbcRepository userRoleJdbcRepository;

    AuthoritiesFetcherService(UserRoleJdbcRepository userRoleJdbcRepository) { // <2>
        this.userRoleJdbcRepository = userRoleJdbcRepository;
    }

    @Override
    public List<String> findAuthoritiesByUsername(String username) {
        return userRoleJdbcRepository.findAllAuthoritiesByUsername(username);
    }
}
