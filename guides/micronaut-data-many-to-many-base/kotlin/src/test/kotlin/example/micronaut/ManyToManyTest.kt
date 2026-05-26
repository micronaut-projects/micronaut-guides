/*
 * Copyright 2017-2026 original authors
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

import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test

@MicronautTest(startApplication = false) // <1>
class ManyToManyTest {

    @Test
    fun testManyToManyPersistence(
        roleRepo: RoleJdbcRepository,
        userRepo: UserJdbcRepository,
        userRoleRepo: UserRoleJdbcRepository
    ) {
        val roleUser = roleRepo.save(ROLE_USER)
        val roleAdmin = roleRepo.save(ROLE_ADMIN)

        assertFalse(userRepo.findByUsername(U_SERGIO).isPresent)
        val sergio = userRepo.save(U_SERGIO)
        assertUser(userRepo.findByUsername(U_SERGIO).orElse(null), U_SERGIO, null)
        userRoleRepo.save(UserRole(sergio, roleUser))
        userRoleRepo.save(UserRole(sergio, roleAdmin))
        assertUser(userRepo.findByUsername(U_SERGIO).orElse(null), U_SERGIO, listOf(ROLE_ADMIN, ROLE_USER))

        val tim = userRepo.save(U_TIM)
        userRoleRepo.save(UserRole(tim, roleUser))
        assertUser(userRepo.findByUsername(U_TIM).orElse(null), U_TIM, listOf(ROLE_USER))

        userRoleRepo.delete(UserRole(tim, roleUser))
        userRoleRepo.delete(UserRole(sergio, roleUser))
        userRoleRepo.delete(UserRole(sergio, roleAdmin))

        userRepo.delete(sergio)
        userRepo.delete(tim)

        roleRepo.deleteByAuthority(ROLE_ADMIN)
        roleRepo.deleteByAuthority(ROLE_USER)
    }

    private fun assertUser(user: User?, expectedUsername: String, expectedAuthorities: List<String>?) {
        assertNotNull(user)
        val actual = user!!
        assertNotNull(actual.id)
        assertEquals(expectedUsername, actual.username)
        assertEquals(expectedAuthorities, actual.authorities)
    }

    private companion object {
        const val ROLE_USER = "ROLE_USER"
        const val ROLE_ADMIN = "ROLE_ADMIN"
        const val U_SERGIO = "sergio"
        const val U_TIM = "tim"
    }
}
