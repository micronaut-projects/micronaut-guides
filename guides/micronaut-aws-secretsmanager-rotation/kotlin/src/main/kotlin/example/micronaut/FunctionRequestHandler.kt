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

import com.amazonaws.services.lambda.runtime.events.SecretsManagerRotationEvent
import io.micronaut.aws.distributedconfiguration.KeyValueFetcher
import io.micronaut.core.annotation.Introspected
import io.micronaut.function.aws.MicronautRequestHandler
import io.micronaut.json.JsonMapper
import jakarta.inject.Inject
import org.slf4j.LoggerFactory
import software.amazon.awssdk.services.secretsmanager.SecretsManagerClient
import software.amazon.awssdk.services.secretsmanager.model.PutSecretValueRequest
import tools.jackson.core.JacksonException
import java.io.IOException
import java.util.Optional

@Introspected
class FunctionRequestHandler : MicronautRequestHandler<SecretsManagerRotationEvent, Void?>() { // <1>

    @Inject
    lateinit var jsonWebKeyGenerator: JsonWebKeyGenerator // <2>

    @Inject
    lateinit var objectMapper: JsonMapper // <3>

    @Inject
    lateinit var secretsManagerClient: SecretsManagerClient // <4>

    @Inject
    lateinit var keyValueFetcher: KeyValueFetcher // <5>

    override fun execute(input: SecretsManagerRotationEvent): Void? {
        LOG.info("step {} secretId: {}", input.step, input.secretId)
        SecretsManagerRotationStep.of(input.step).ifPresent { step ->
            if (step == SecretsManagerRotationStep.FINISH_SECRET) {
                currentPrimary(input.secretId)
                    .flatMap { generateSecretString(it) }
                    .ifPresent { updateSecretString(input, it) }
            }
        }
        return null
    }

    private fun currentPrimary(secretId: String): Optional<String> = // <6>
        keyValueFetcher.keyValuesByPrefix(secretId)
            .filter { it.containsKey(JWK_PRIMARY) }
            .flatMap { Optional.ofNullable(it[JWK_PRIMARY]) }
            .map { it.toString() }

    private fun generateSecretString(currentPrimary: String): Optional<String> { // <7>
        val jsonJwkOptional = jsonWebKeyGenerator.generateJsonWebKey(null)
        if (jsonJwkOptional.isEmpty) {
            return Optional.empty()
        }
        val newJwk = mapOf(
            JWK_PRIMARY to jsonJwkOptional.get(),
            JWK_SECONDARY to currentPrimary
        )
        return try {
            Optional.of(objectMapper.writeValueAsString(newJwk))
        } catch (e: JacksonException) {
            LOG.warn("JacksonException", e)
            Optional.empty()
        } catch (e: IOException) {
            LOG.warn("IOException", e)
            Optional.empty()
        }
    }

    private fun updateSecretString(input: SecretsManagerRotationEvent, secretString: String) { // <8>
        secretsManagerClient.putSecretValue(PutSecretValueRequest.builder()
            .clientRequestToken(input.clientRequestToken)
            .secretId(input.secretId)
            .secretString(secretString)
            .build())
    }

    companion object {
        private const val JWK_PRIMARY = "jwk.primary"
        private const val JWK_SECONDARY = "jwk.secondary"

        private val LOG = LoggerFactory.getLogger(FunctionRequestHandler::class.java)
    }
}
