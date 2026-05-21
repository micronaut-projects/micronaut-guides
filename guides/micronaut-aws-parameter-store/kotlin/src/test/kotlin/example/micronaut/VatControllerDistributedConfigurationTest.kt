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

import io.micronaut.context.env.Environment
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import io.micronaut.test.support.TestPropertyProvider
import jakarta.inject.Inject
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.testcontainers.containers.localstack.LocalStackContainer
import org.testcontainers.utility.DockerImageName
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.ssm.SsmClient
import software.amazon.awssdk.services.ssm.model.ParameterType
import software.amazon.awssdk.services.ssm.model.PutParameterRequest

@MicronautTest(environments = [Environment.AMAZON_EC2]) // <1>
@TestInstance(TestInstance.Lifecycle.PER_CLASS) // <2>
class VatControllerDistributedConfigurationTest : TestPropertyProvider { // <3>

    @Inject
    @field:Client("/")
    lateinit var httpClient: HttpClient

    override fun getProperties(): MutableMap<String, String> {
        if (!localstack.isRunning) {
            localstack.start()
        }
        SsmClient.builder()
            .endpointOverride(localstack.getEndpointOverride(LocalStackContainer.Service.SSM))
            .credentialsProvider(
                StaticCredentialsProvider.create(
                    AwsBasicCredentials.create(localstack.accessKey, localstack.secretKey)
                )
            )
            .region(Region.of(localstack.region))
            .build()
            .use { ssmClient ->
                ssmClient.putParameter(
                    PutParameterRequest.builder()
                        .name("/config/micronautguide/vat/country/")
                        .type(ParameterType.STRING)
                        .value("Spain")
                        .build()
                )
                ssmClient.putParameter(
                    PutParameterRequest.builder()
                        .name("/config/micronautguide/vat/rate/")
                        .type(ParameterType.STRING)
                        .value("21")
                        .build()
                )
            }
        return mapOf(
            "aws.access-key-id" to localstack.accessKey,
            "aws.secret-key" to localstack.secretKey,
            "aws.region" to localstack.region,
            "aws.services.ssm.endpoint-override" to localstack.getEndpointOverride(LocalStackContainer.Service.SSM)
                .toString()
        ).toMutableMap()
    }

    @Test
    fun vatExposesTheValueAddedTaxRate() {
        assertEquals("{\"rate\":21}", httpClient.toBlocking().retrieve("/vat"))
    }

    companion object {
        private val localstackImage: DockerImageName = DockerImageName.parse("localstack/localstack:4.14.0")
        private val localstack: LocalStackContainer = LocalStackContainer(localstackImage)
            .withServices(LocalStackContainer.Service.SSM)
    }
}
