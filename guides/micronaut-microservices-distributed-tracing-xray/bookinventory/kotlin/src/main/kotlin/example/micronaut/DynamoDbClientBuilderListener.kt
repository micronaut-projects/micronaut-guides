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

import io.micronaut.context.annotation.Requires
import io.micronaut.context.annotation.Value
import io.micronaut.context.event.BeanCreatedEvent
import io.micronaut.context.event.BeanCreatedEventListener
import io.micronaut.context.exceptions.ConfigurationException
import jakarta.inject.Singleton
import software.amazon.awssdk.auth.credentials.AwsCredentials
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.dynamodb.DynamoDbClientBuilder
import java.net.URI
import java.net.URISyntaxException

@Requires(property = "dynamodb-local.host")
@Requires(property = "dynamodb-local.port")
@Singleton
class DynamoDbClientBuilderListener(
    @Value("\${dynamodb-local.host}") host: String,
    @Value("\${dynamodb-local.port}") port: String
) : BeanCreatedEventListener<DynamoDbClientBuilder> {
    private val endpoint: URI = try {
        URI("http://$host:$port")
    } catch (e: URISyntaxException) {
        throw ConfigurationException("dynamodb.endpoint not a valid URI")
    }
    private val accessKeyId = "fakeMyKeyId"
    private val secretAccessKey = "fakeSecretAccessKey"

    override fun onCreated(event: BeanCreatedEvent<DynamoDbClientBuilder>): DynamoDbClientBuilder =
        event.bean.endpointOverride(endpoint)
            .region(Region.US_EAST_1)
            .credentialsProvider {
                object : AwsCredentials {
                    override fun accessKeyId(): String = accessKeyId

                    override fun secretAccessKey(): String = secretAccessKey
                }
            }
}
