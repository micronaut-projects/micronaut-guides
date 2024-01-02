package example.micronaut;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import io.micronaut.json.JsonMapper;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.*;

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
    public void testHandler() throws IOException {
        JsonMapper jsonMapper = handler.getApplicationContext().getBean(JsonMapper.class);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (InputStream inputStream = createInputStreamRequest(jsonMapper)) {
            handler.execute(inputStream, baos); // <3>
            assertEquals("""
                {"statusCode":200,"body":"{\\"message\\":\\"Hello World\\"}"}""", baos.toString());
        }

    }

    private InputStream createInputStreamRequest(JsonMapper jsonMapper) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        jsonMapper.writeValue(outputStream, createRequest());
        return new ByteArrayInputStream(outputStream.toByteArray());
    }

    private APIGatewayProxyRequestEvent createRequest() {
        APIGatewayProxyRequestEvent request = new APIGatewayProxyRequestEvent();
        request.setHttpMethod("GET");
        request.setPath("/");
        return request;
    }
}
