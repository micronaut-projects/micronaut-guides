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

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class FunctionRequestHandlerTest {

    @Test
    fun testHandler() {
        val handler = FunctionRequestHandler()
        val request = APIGatewayProxyRequestEvent()
        request.httpMethod = "GET"
        request.path = "/"
        val response = handler.execute(request)
        assertEquals(200, response.statusCode.toInt())
        assertEquals("{\"message\":\"Hello World\"}", response.body)
        handler.close()
    }
}
