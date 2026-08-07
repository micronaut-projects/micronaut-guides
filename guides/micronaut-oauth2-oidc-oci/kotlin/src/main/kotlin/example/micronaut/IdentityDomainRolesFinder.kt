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

import io.micronaut.context.annotation.Replaces
import io.micronaut.security.token.RolesFinder
import io.micronaut.security.token.config.TokenConfiguration
import jakarta.inject.Singleton

@Singleton // <1>
@Replaces(RolesFinder::class)
class IdentityDomainRolesFinder(
    private val tokenConfiguration: TokenConfiguration
) : RolesFinder {

    override fun resolveRoles(attributes: Map<String, Any>?): List<String> {
        if (attributes == null || !attributes.containsKey(tokenConfiguration.rolesName)) {
            return emptyList()
        }
        val obj = attributes[tokenConfiguration.rolesName] ?: return emptyList()
        if (obj is Iterable<*>) {
            return obj.mapNotNull { element ->
                if (element is Map<*, *>) {
                    element[KEY_NAME]?.toString()?.takeIf { it.isNotEmpty() }
                } else {
                    null
                }
            }
        }
        return emptyList()
    }

    companion object {
        private const val KEY_NAME = "name"
    }
}
