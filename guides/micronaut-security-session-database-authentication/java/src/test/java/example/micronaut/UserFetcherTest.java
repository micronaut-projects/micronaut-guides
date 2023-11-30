package example.micronaut;

import example.micronaut.repositories.RoleJdbcRepository;
import example.micronaut.repositories.UserJdbcRepository;
import example.micronaut.repositories.UserRoleJdbcRepository;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@MicronautTest(startApplication = false, transactional = false)
class UserFetcherTest {
    @Test
    void findAuthoritiesByUsername(RegisterService registerService,
                                   UserJdbcRepository userRepository,
                                   UserRoleJdbcRepository userRoleRepository,
                                   RoleJdbcRepository roleRepository,
                                   UserFetcher userFetcher) {
        String username = "user";
        String rawPassword = "user123";
        assertDoesNotThrow(() -> registerService.register(username, rawPassword, Arrays.asList("ROLE_USER", "ROLE_ADMIN")));
        Optional<UserState> userStateOptional = userFetcher.findByUsername(username);
        assertTrue(userStateOptional.isPresent());
        UserState userState = userStateOptional.get();
        assertNotNull(userState);
        assertEquals(username, userState.getUsername());
        assertNotEquals(rawPassword, userState.getPassword());
        assertTrue(userState.isEnabled());
        assertFalse(userState.isAccountLocked());
        assertFalse(userState.isAccountExpired());
        assertFalse(userState.isPasswordExpired());
        userRoleRepository.deleteAll();
        roleRepository.deleteAll();
        userRepository.deleteAll();
    }

}