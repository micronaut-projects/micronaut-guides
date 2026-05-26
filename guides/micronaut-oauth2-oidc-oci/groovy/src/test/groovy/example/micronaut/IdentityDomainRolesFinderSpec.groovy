package example.micronaut

import io.micronaut.security.token.RolesFinder
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import jakarta.inject.Inject
import spock.lang.Specification

@MicronautTest(startApplication = false)
class IdentityDomainRolesFinderSpec extends Specification {

    @Inject
    RolesFinder rolesFinder

    void 'test find roles'() {
        expect:
        rolesFinder instanceof IdentityDomainRolesFinder

        when:
        List<String> roles = rolesFinder.resolveRoles([
                groups: [[name: 'ROLE_ADMIN', id: 'cab3a10ad56935ca1726464bcaa12a34']]
        ])

        then:
        roles == ['ROLE_ADMIN']

        when:
        roles = rolesFinder.resolveRoles(Collections.emptyMap())

        then:
        roles == Collections.emptyList()

        when:
        roles = rolesFinder.resolveRoles([groups: 'foo'])

        then:
        roles == Collections.emptyList()
    }
}
