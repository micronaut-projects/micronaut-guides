package example.micronaut

import io.micronaut.test.extensions.spock.annotation.MicronautTest
import jakarta.inject.Inject
import spock.lang.Specification

@MicronautTest(startApplication = false) // <1>
class ManyToManySpec extends Specification {

    private static final String ROLE_USER = 'ROLE_USER'
    private static final String ROLE_ADMIN = 'ROLE_ADMIN'
    private static final String U_SERGIO = 'sergio'
    private static final String U_TIM = 'tim'

    @Inject
    RoleJdbcRepository roleRepo

    @Inject
    UserJdbcRepository userRepo

    @Inject
    UserRoleJdbcRepository userRoleRepo

    void "test many-to-many persistence"() {
        when:
        Role roleUser = roleRepo.save(ROLE_USER)
        Role roleAdmin = roleRepo.save(ROLE_ADMIN)

        then:
        !userRepo.findByUsername(U_SERGIO).present

        when:
        UserEntity sergio = userRepo.save(U_SERGIO)

        then:
        assertUser(userRepo.findByUsername(U_SERGIO).orElse(null), U_SERGIO, null)

        when:
        userRoleRepo.save(new UserRole(sergio, roleUser))
        userRoleRepo.save(new UserRole(sergio, roleAdmin))

        then:
        assertUser(userRepo.findByUsername(U_SERGIO).orElse(null), U_SERGIO, [ROLE_ADMIN, ROLE_USER])

        when:
        UserEntity tim = userRepo.save(U_TIM)
        userRoleRepo.save(new UserRole(tim, roleUser))

        then:
        assertUser(userRepo.findByUsername(U_TIM).orElse(null), U_TIM, [ROLE_USER])

        cleanup:
        userRoleRepo.delete(new UserRole(tim, roleUser))
        userRoleRepo.delete(new UserRole(sergio, roleUser))
        userRoleRepo.delete(new UserRole(sergio, roleAdmin))
        userRepo.delete(sergio)
        userRepo.delete(tim)
        roleRepo.deleteByAuthority(ROLE_ADMIN)
        roleRepo.deleteByAuthority(ROLE_USER)
    }

    private static void assertUser(User user, String expectedUsername, List<String> expectedAuthorities) {
        assert user
        assert user.id
        assert user.username == expectedUsername
        assert user.authorities == expectedAuthorities
    }
}
