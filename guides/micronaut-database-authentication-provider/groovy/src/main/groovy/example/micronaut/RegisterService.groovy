package example.micronaut

import example.micronaut.domain.Role
import example.micronaut.domain.User
import grails.gorm.transactions.Transactional
import groovy.transform.CompileStatic

import javax.inject.Singleton
import javax.validation.constraints.Email
import javax.validation.constraints.NotBlank

@CompileStatic
@Singleton
class RegisterService {

    private final RoleGormService roleGormService
    private final UserGormService userGormService
    private final UserRoleGormService userRoleGormService
    private final PasswordEncoder passwordEncoder

    RegisterService(RoleGormService roleGormService,
                    UserGormService userGormService,
                    PasswordEncoder passwordEncoder,
                    UserRoleGormService userRoleGormService) {
        this.roleGormService = roleGormService
        this.userGormService = userGormService
        this.userRoleGormService = userRoleGormService
        this.passwordEncoder = passwordEncoder
    }

    @Transactional
    void register(@Email String email, @NotBlank String username,
                  @NotBlank String rawPassword, List<String> authorities) {

        User user = userGormService.findByUsername(username)
        if (!user) {
            final String encodedPassword = passwordEncoder.encode(rawPassword)
            user = userGormService.save(email, username, encodedPassword)
        }

        if (user && authorities) {
            for (String authority : authorities) {
                Role role = roleGormService.find(authority) ?: roleGormService.save(authority)
                userRoleGormService.find(user, role) ?: userRoleGormService.save(user, role)
            }
        }
    }
}
