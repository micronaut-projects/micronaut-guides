package example.micronaut;

import example.micronaut.repositories.RoleJdbcRepository;
import example.micronaut.repositories.UserJdbcRepository;
import example.micronaut.repositories.UserRoleJdbcRepository;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.Test;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

@MicronautTest(startApplication = false, transactional = false)
class RegisterServiceTest {

    @Test
    void register(RegisterService registerService,
                  UserJdbcRepository userRepository,
                  UserRoleJdbcRepository userRoleRepository,
                  RoleJdbcRepository roleRepository) {
        String username = "admin";
        assertDoesNotThrow(() -> registerService.register(username, "admin123", Arrays.asList("ROLE_USER", "ROLE_ADMIN")));
        assertEquals(1, userRepository.count());
        assertEquals(2, roleRepository.count());
        assertEquals(2, userRoleRepository.count());
        assertEquals(2, userRoleRepository.findAllAuthoritiesByUsername(username).size());
        userRoleRepository.deleteAll();
        roleRepository.deleteAll();
        userRepository.deleteAll();
    }
}