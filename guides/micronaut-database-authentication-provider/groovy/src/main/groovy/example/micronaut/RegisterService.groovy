/*
 * Copyright 2017-2024 original authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package example.micronaut

import example.micronaut.domain.Role
import example.micronaut.domain.User
import example.micronaut.domain.UserRole
import example.micronaut.domain.UserRoleId
import groovy.transform.CompileStatic

import jakarta.inject.Singleton
import jakarta.transaction.Transactional
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank

@CompileStatic
@Singleton
class RegisterService {

    private final RoleJdbcRepository roleGormService
    private final UserJdbcRepository userGormService
    private final UserRoleJdbcRepository userRoleGormService
    private final PasswordEncoder passwordEncoder

    RegisterService(RoleJdbcRepository roleGormService,
                    UserJdbcRepository userGormService,
                    PasswordEncoder passwordEncoder,
                    UserRoleJdbcRepository userRoleGormService) {
        this.roleGormService = roleGormService
        this.userGormService = userGormService
        this.userRoleGormService = userRoleGormService
        this.passwordEncoder = passwordEncoder
    }

    @Transactional
    void register(@Email String email, @NotBlank String username,
                  @NotBlank String rawPassword, List<String> authorities) {

        User user = userGormService.findByUsername(username).orElse(null)
        if (!user) {
            final String encodedPassword = passwordEncoder.encode(rawPassword)
            user = userGormService.save(new User(email: email, username: username, password: encodedPassword, enabled: true, accountExpired: false, accountLocked: false, passwordExpired: false))
        }

        if (user && authorities) {
            for (String authority : authorities) {
                Role role = roleGormService.findByAuthority(authority).orElseGet(() -> roleGormService.save(authority))
                UserRoleId userRoleId = new UserRoleId(user.id, role.id)
                if (userRoleGormService.findById(userRoleId).isEmpty()) {
                    userRoleGormService.save(new UserRole(userRoleId))
                }
            }
        }
    }
}
