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
import io.micronaut.json.JsonMapper
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.InputStream

class FunctionRequestHandlerTest {

    @Test
    fun testHandler() {
        val jsonMapper = handler.applicationContext.getBean(JsonMapper::class.java)
        ByteArrayOutputStream().use { baos ->
            createInputStreamRequest(jsonMapper).use { inputStream ->
                handler.execute(inputStream, baos) // <3>
                assertEquals(
                    """{"statusCode":200,"body":"{\"message\":\"Hello World\"}"}""",
                    baos.toString()
                )
            }
        }
    }

    private fun createInputStreamRequest(jsonMapper: JsonMapper): InputStream =
        ByteArrayInputStream(jsonMapper.writeValueAsBytes(createRequest()))

    private fun createRequest(): APIGatewayProxyRequestEvent =
        APIGatewayProxyRequestEvent()
            .withHttpMethod("GET")
            .withPath("/")

    companion object {
        private lateinit var handler: FunctionRequestHandler

        @BeforeAll
        @JvmStatic
        fun setupServer() {
            handler = FunctionRequestHandler() // <1>
        }

        @AfterAll
        @JvmStatic
        fun stopServer() {
            if (::handler.isInitialized) {
                handler.applicationContext.close() // <2>
            }
        }
    }
}
