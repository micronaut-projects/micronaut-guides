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
import software.amazon.awssdk.services.dynamodb.DynamoDbClientBuilder
import java.net.URI
import java.net.URISyntaxException

@Requires(property = "dynamodb-local.host") // <1>
@Requires(property = "dynamodb-local.port") // <1>
@Singleton // <2>
class DynamoDbClientBuilderListener(
    @Value("\${dynamodb-local.host}") host: String, // <4>
    @Value("\${dynamodb-local.port}") port: String // <4>
) : BeanCreatedEventListener<DynamoDbClientBuilder> { // <3>

    private val endpoint: URI
    private val accessKeyId = "fakeMyKeyId"
    private val secretAccessKey = "fakeSecretAccessKey"

    init {
        endpoint = try {
            URI("http://$host:$port")
        } catch (e: URISyntaxException) {
            throw ConfigurationException("dynamodb.endpoint not a valid URI")
        }
    }

    override fun onCreated(event: BeanCreatedEvent<DynamoDbClientBuilder>): DynamoDbClientBuilder =
        event.bean.endpointOverride(endpoint)
            .credentialsProvider {
                object : AwsCredentials {
                    override fun accessKeyId(): String = accessKeyId

                    override fun secretAccessKey(): String = secretAccessKey
                }
            }
}
