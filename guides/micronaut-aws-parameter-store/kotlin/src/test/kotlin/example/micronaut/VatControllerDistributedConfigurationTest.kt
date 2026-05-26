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

import io.floci.testcontainers.FlociContainer
import io.micronaut.context.env.Environment
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import io.micronaut.test.support.TestPropertyProvider
import jakarta.inject.Inject
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.testcontainers.utility.DockerImageName
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.ssm.SsmClient
import software.amazon.awssdk.services.ssm.model.ParameterType
import software.amazon.awssdk.services.ssm.model.PutParameterRequest
import java.net.URI

@MicronautTest(environments = [Environment.AMAZON_EC2]) // <1>
@TestInstance(TestInstance.Lifecycle.PER_CLASS) // <2>
class VatControllerDistributedConfigurationTest : TestPropertyProvider { // <3>

    @Inject
    @field:Client("/")
    lateinit var httpClient: HttpClient

    override fun getProperties(): MutableMap<String, String> {
        if (!floci.isRunning) {
            floci.start()
        }
        configureSsmProperties()
        SsmClient.builder()
            .endpointOverride(URI.create(floci.endpoint))
            .credentialsProvider(
                StaticCredentialsProvider.create(
                    AwsBasicCredentials.create(floci.accessKey, floci.secretKey)
                )
            )
            .region(Region.of(floci.region))
            .build()
            .use { ssmClient ->
                ssmClient.putParameter(
                    PutParameterRequest.builder()
                        .name("/config/micronautguide/vat/country")
                        .type(ParameterType.STRING)
                        .value("Spain")
                        .build()
                )
                ssmClient.putParameter(
                    PutParameterRequest.builder()
                        .name("/config/micronautguide/vat/rate")
                        .type(ParameterType.STRING)
                        .value("21")
                        .build()
                )
            }
        return mapOf(
            "aws.access-key-id" to floci.accessKey,
            "aws.secret-key" to floci.secretKey,
            "aws.region" to floci.region,
            "aws.services.ssm.endpoint-override" to floci.endpoint,
            "micronaut.config.import" to "parameterstore:///config/micronautguide"
        ).toMutableMap()
    }

    private fun configureSsmProperties() {
        System.setProperty("aws.access-key-id", floci.accessKey)
        System.setProperty("aws.secret-key", floci.secretKey)
        System.setProperty("aws.region", floci.region)
        System.setProperty("aws.services.ssm.endpoint-override", floci.endpoint)
    }

    @Test
    fun vatExposesTheValueAddedTaxRate() {
        assertEquals("{\"rate\":21}", httpClient.toBlocking().retrieve("/vat"))
    }

    companion object {
        private val floci: FlociContainer = FlociContainer(DockerImageName.parse("floci/floci:1.5.18"))
    }
}
