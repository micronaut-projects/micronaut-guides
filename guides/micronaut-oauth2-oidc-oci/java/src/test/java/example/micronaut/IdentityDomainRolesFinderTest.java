package example.micronaut;

import io.micronaut.security.token.RolesFinder;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.Test;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;

@MicronautTest(startApplication = false)
class IdentityDomainRolesFinderTest {

    @Test
    void testFindRoles(RolesFinder rolesFinder) {
        assertInstanceOf(IdentityDomainRolesFinder.class, rolesFinder);
        List<String> roles = rolesFinder.resolveRoles(Map.of("groups", List.of(Map.of("name", "ROLE_ADMIN", "id", "cab3a10ad56935ca1726464bcaa12a34"))));
        List<String> expectedRoles = List.of("ROLE_ADMIN");
        assertEquals(expectedRoles, roles);

        roles = rolesFinder.resolveRoles(Collections.emptyMap());
        expectedRoles = Collections.emptyList();
        assertEquals(expectedRoles, roles);

        roles = rolesFinder.resolveRoles(Map.of("groups", "foo"));
        assertEquals(expectedRoles, roles);
    }

}