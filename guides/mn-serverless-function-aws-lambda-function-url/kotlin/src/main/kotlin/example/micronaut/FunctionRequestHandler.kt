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

import io.micronaut.function.aws.MicronautRequestHandler
import java.io.IOException
import io.micronaut.json.JsonMapper
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent
import jakarta.inject.Inject

class FunctionRequestHandler : MicronautRequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent>() {

    @Inject
    lateinit var objectMapper: JsonMapper

    override fun execute(input: APIGatewayProxyRequestEvent): APIGatewayProxyResponseEvent =
        APIGatewayProxyResponseEvent().apply {
            try {
                val json = String(objectMapper.writeValueAsBytes(mapOf("message" to "Hello World")))
                statusCode = 200
                body = json
            } catch (e: IOException) {
                statusCode = 500
            }
        }
}
