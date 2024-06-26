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

import com.nimbusds.jwt.JWTParser
import com.nimbusds.jwt.SignedJWT
import io.micronaut.security.authentication.UsernamePasswordCredentials
import io.micronaut.security.token.render.BearerAccessRefreshToken
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import jakarta.inject.Inject
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

@MicronautTest
class DeclarativeHttpClientWithJwtTest {

    @Inject
    lateinit var appClient: AppClient // <1>

    @Test
    fun verifyJwtAuthenticationWorksWithDeclarativeClient() {
        val creds: UsernamePasswordCredentials = UsernamePasswordCredentials("sherlock", "password")
        val loginRsp: BearerAccessRefreshToken = appClient.login(creds) // <2>

        assertNotNull(loginRsp)
        assertNotNull(loginRsp.accessToken)
        assertTrue(JWTParser.parse(loginRsp.accessToken) is SignedJWT)

        val msg = appClient.home("Bearer ${loginRsp.accessToken}") // <3>
        assertEquals("sherlock", msg)
    }
}
