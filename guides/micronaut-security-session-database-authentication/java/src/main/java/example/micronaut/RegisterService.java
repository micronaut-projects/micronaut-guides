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
package example.micronaut;

import example.micronaut.entities.Role;
import example.micronaut.entities.User;
import example.micronaut.entities.UserRole;
import example.micronaut.entities.UserRoleId;
import example.micronaut.exceptions.UserAlreadyExistsException;
import example.micronaut.repositories.RoleJdbcRepository;
import example.micronaut.repositories.UserJdbcRepository;
import example.micronaut.repositories.UserRoleJdbcRepository;
import io.micronaut.core.annotation.Nullable;
import jakarta.inject.Singleton;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotBlank;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Singleton // <1>
public class RegisterService {

    private static final boolean DEFAULT_ENABLED = true;
    private static final boolean DEFAULT_ACCOUNT_EXPIRED = false;

    private static final boolean DEFAULT_ACCOUNT_LOCKED = false;

    private static final boolean DEFAULT_PASSWORD_EXPIRED = false;
    private final RoleJdbcRepository roleService;
    private final UserJdbcRepository userJdbcRepository;
    private final UserRoleJdbcRepository userRoleJdbcRepository;
    private final PasswordEncoder passwordEncoder;

    public RegisterService(RoleJdbcRepository roleGormService,
                    UserJdbcRepository userJdbcRepository,
                    PasswordEncoder passwordEncoder,
                    UserRoleJdbcRepository userRoleJdbcRepository) {
        this.roleService = roleGormService;
        this.userJdbcRepository = userJdbcRepository;
        this.userRoleJdbcRepository = userRoleJdbcRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void register(@NotBlank String username,
                         @NotBlank String rawPassword) {
        register(username, rawPassword, Collections.emptyList());
    }

    @Transactional // <2>
    public void register(@NotBlank String username,
                         @NotBlank String rawPassword,
                         @Nullable List<String> authorities) {
        Optional<User> userOptional = userJdbcRepository.findByUsername(username);
        if (userOptional.isPresent()) {
            throw new UserAlreadyExistsException();
        }
        User user = userJdbcRepository.save(createUser(username, rawPassword));
        if (user != null && authorities != null) {
            for (String authority : authorities) {
                Role role = roleService.findByAuthority(authority).orElseGet(() -> roleService.save(authority));
                UserRoleId userRoleId = new UserRoleId(user, role);
                if (userRoleJdbcRepository.findById(userRoleId).isEmpty()) {
                    userRoleJdbcRepository.save(new UserRole(userRoleId));
                }
            }
        }
    }

    private User createUser(String username, String rawPassword) {
        final String encodedPassword = passwordEncoder.encode(rawPassword);
        return new User(null,
                username,
                encodedPassword,
                DEFAULT_ENABLED,
                DEFAULT_ACCOUNT_EXPIRED,
                DEFAULT_ACCOUNT_LOCKED,
                DEFAULT_PASSWORD_EXPIRED);
    }
}
