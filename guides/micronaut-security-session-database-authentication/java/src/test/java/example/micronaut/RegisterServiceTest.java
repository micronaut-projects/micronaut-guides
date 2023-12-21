/*
 * Copyright 2017-2023 original authors
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