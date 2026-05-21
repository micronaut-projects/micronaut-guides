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

import io.micronaut.context.annotation.BootstrapContextCompatible
import io.micronaut.context.annotation.Factory
import io.micronaut.context.annotation.Primary
import jakarta.inject.Singleton
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.ssm.SsmAsyncClient
import software.amazon.awssdk.services.ssm.SsmAsyncClientBuilder
import java.net.URI
import java.net.URISyntaxException

@Factory
@BootstrapContextCompatible
class SsmAsyncClientFactory(private val config: SsmConfig) {

    @Primary
    @Singleton
    fun createSecretsManagerClient(builder: SsmAsyncClientBuilder): SsmAsyncClient {
        try {
            return builder
                .endpointOverride(URI(config.ssm.endpointOverride!!))
                .credentialsProvider(
                    StaticCredentialsProvider.create(
                        AwsBasicCredentials.create(config.accessKeyId!!, config.secretKey!!)
                    )
                )
                .region(Region.of(config.region!!))
                .build()
        } catch (e: URISyntaxException) {
            throw RuntimeException(e)
        }
    }
}
