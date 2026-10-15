package example.micronaut;

import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@MicronautTest(startApplication = false) // <1>
class ManyToManyTest {

    private static final String ROLE_USER = "ROLE_USER";
    private static final String ROLE_ADMIN = "ROLE_ADMIN";
    private static final String U_SERGIO = "sergio";
    private static final String U_TIM = "tim";

    @Test
    void testManyToManyPersistence(RoleJdbcRepository roleRepo,
              UserJdbcRepository userRepo,
              UserRoleJdbcRepository userRoleRepo) {
        Role roleUser = roleRepo.save(ROLE_USER);
        Role roleAdmin = roleRepo.save(ROLE_ADMIN);

        assertFalse(userRepo.findByUsername(U_SERGIO).isPresent());
        UserEntity sergio = userRepo.save(U_SERGIO);
        assertUser(userRepo.findByUsername(U_SERGIO).orElse(null), U_SERGIO, null);
        userRoleRepo.save(new UserRole(sergio, roleUser));
        userRoleRepo.save(new UserRole(sergio, roleAdmin));
        assertUser(userRepo.findByUsername(U_SERGIO).orElse(null), U_SERGIO, List.of(ROLE_ADMIN, ROLE_USER));

        UserEntity tim = userRepo.save(U_TIM);
        userRoleRepo.save(new UserRole(tim, roleUser));
        assertUser(userRepo.findByUsername(U_TIM).orElse(null), U_TIM, List.of(ROLE_USER));

        userRoleRepo.delete(new UserRole(tim, roleUser));
        userRoleRepo.delete(new UserRole(sergio, roleUser));
        userRoleRepo.delete(new UserRole(sergio, roleAdmin));

        userRepo.delete(sergio);
        userRepo.delete(tim);

        roleRepo.deleteByAuthority(ROLE_ADMIN);
        roleRepo.deleteByAuthority(ROLE_USER);
    }

    void assertUser(User user, String expectedUsername, List<String> expectedAuthorities) {
        assertNotNull(user);
        assertNotNull(user.id());
        assertEquals(expectedUsername, user.username());
        assertEquals(expectedAuthorities, user.authorities());
    }
}
