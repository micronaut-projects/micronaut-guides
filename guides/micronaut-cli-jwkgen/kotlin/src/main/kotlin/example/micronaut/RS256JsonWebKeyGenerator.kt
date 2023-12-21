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
package example.micronaut

import com.nimbusds.jose.JOSEException
import com.nimbusds.jose.JWSAlgorithm.RS256
import com.nimbusds.jose.jwk.KeyUse.SIGNATURE
import com.nimbusds.jose.jwk.gen.RSAKeyGenerator
import jakarta.inject.Singleton
import org.slf4j.LoggerFactory
import java.util.Optional
import java.util.UUID

@Singleton // <1>
class RS256JsonWebKeyGenerator : JsonWebKeyGenerator {

    override fun generateJsonWebKey(kid: String?): Optional<String> {
        return try {
            Optional.of(RSAKeyGenerator(2048)
                    .algorithm(RS256)
                    .keyUse(SIGNATURE) // indicate the intended use of the key
                    .keyID(kid ?: generateKid()) // give the key a unique ID
                    .generate()
                    .toJSONString())
        } catch (e: JOSEException) {
            LOG.warn("unable to generate RS256 key", e)
            Optional.empty()
        }
    }

    private fun generateKid(): String = UUID.randomUUID().toString().replace("-".toRegex(), "")

    companion object {
        private val LOG = LoggerFactory.getLogger(RS256JsonWebKeyGenerator::class.java)
    }
}
