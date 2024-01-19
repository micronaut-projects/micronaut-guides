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
package example.micronaut;

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import io.micronaut.json.JsonMapper;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FunctionRequestHandlerTest {

    private static FunctionRequestHandler handler;

    @BeforeAll
    public static void setupServer() {
        handler = new FunctionRequestHandler(); // <1>
    }

    @AfterAll
    public static void stopServer() {
        if (handler != null) {
            handler.getApplicationContext().close(); // <2>
        }
    }

    @Test
    void testHandler() throws IOException {
        JsonMapper jsonMapper = handler.getApplicationContext().getBean(JsonMapper.class);
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
             InputStream inputStream = createInputStreamRequest(jsonMapper)) {
            handler.execute(inputStream, baos); // <3>
            assertEquals("""
                    {"statusCode":200,"body":"{\\"message\\":\\"Hello World\\"}"}""", baos.toString());
        }
    }

    private InputStream createInputStreamRequest(JsonMapper jsonMapper) throws IOException {
        return new ByteArrayInputStream(jsonMapper.writeValueAsBytes(createRequest()));
    }

    private APIGatewayProxyRequestEvent createRequest() {
        APIGatewayProxyRequestEvent request = new APIGatewayProxyRequestEvent();
        request.setHttpMethod("GET");
        request.setPath("/");
        return request;
    }
}
