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

import com.nimbusds.jose.JOSEException
import com.nimbusds.jose.JWSAlgorithm
import com.nimbusds.jose.jwk.JWK
import com.nimbusds.jose.jwk.RSAKey
import io.micronaut.context.exceptions.ConfigurationException
import io.micronaut.security.token.jwt.signature.rsa.RSASignatureConfiguration
import org.slf4j.LoggerFactory
import java.security.interfaces.RSAPrivateKey
import java.security.interfaces.RSAPublicKey
import java.text.ParseException
import java.util.Optional

abstract class AbstractRSASignatureConfiguration(jsonJwk: String) : RSASignatureConfiguration { // <1>

    val publicJWK: JWK
    val pubKey: RSAPublicKey
    val privKey: RSAPrivateKey
    val algorithm: JWSAlgorithm

    init {
        val primaryRSAKey = parseRSAKey(jsonJwk)
                .orElseThrow { ConfigurationException("could not parse primary JWK to RSA Key") }

        publicJWK = primaryRSAKey.toPublicJWK()

        privKey = try {
            primaryRSAKey.toRSAPrivateKey()
        } catch (e: JOSEException) {
            throw ConfigurationException("could not primary RSA private key")
        }

        pubKey = try {
            primaryRSAKey.toRSAPublicKey()
        } catch (e: JOSEException) {
            throw ConfigurationException("could not primary RSA public key")
        }

        algorithm = parseJWSAlgorithm(primaryRSAKey)
                .orElseThrow { ConfigurationException("could not parse JWS Algorithm from RSA Key") }
    }

    override fun getPublicKey(): RSAPublicKey? = pubKey

    open fun getJwsAlgorithm(): JWSAlgorithm = algorithm

    open fun getPrivateKey(): RSAPrivateKey = privKey

    private fun parseJWSAlgorithm(rsaKey: RSAKey): Optional<JWSAlgorithm> {
        val algorithm = rsaKey.algorithm ?: return Optional.empty()
        return if (algorithm is JWSAlgorithm) {
            Optional.of(algorithm)
        } else Optional.of(JWSAlgorithm.parse(algorithm.name))
    }

    private fun parseRSAKey(jsonJwk: String): Optional<RSAKey> {
        return try {
            val jwk = JWK.parse(jsonJwk)
            if (jwk !is RSAKey) {
                LOG.warn("JWK is not an RSAKey")
                return Optional.empty()
            }
            Optional.of(jwk)
        } catch (e: ParseException) {
            LOG.warn("Could not parse JWK JSON string {}", jsonJwk)
            Optional.empty()
        }
    }

    companion object {
        private val LOG = LoggerFactory.getLogger(AbstractRSASignatureConfiguration::class.java)
    }
}
