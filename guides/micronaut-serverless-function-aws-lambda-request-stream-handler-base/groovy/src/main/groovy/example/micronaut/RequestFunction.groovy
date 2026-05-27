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

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent
import io.micronaut.function.FunctionBean
import io.micronaut.json.JsonMapper
import jakarta.inject.Inject

import java.util.function.Function

@FunctionBean('requestfunction') // <1>
class RequestFunction implements Function<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> { // <2>

    @Inject // <3>
    JsonMapper jsonMapper

    @Override
    APIGatewayProxyResponseEvent apply(APIGatewayProxyRequestEvent requestEvent) {
        APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent()
        try {
            String json = new String(jsonMapper.writeValueAsBytes([message: 'Hello World']))
            response.statusCode = 200
            response.body = json
        } catch (IOException e) {
            response.statusCode = 500
        }
        response
    }
}
