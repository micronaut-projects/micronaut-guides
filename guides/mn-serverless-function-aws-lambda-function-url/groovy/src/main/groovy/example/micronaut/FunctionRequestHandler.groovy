package example.micronaut

import io.micronaut.function.aws.MicronautRequestHandler
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import io.micronaut.json.JsonMapper;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent
import jakarta.inject.Inject

class FunctionRequestHandler extends MicronautRequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {
    @Inject
    JsonMapper objectMapper

    @Override
    APIGatewayProxyResponseEvent execute(APIGatewayProxyRequestEvent input) {
        APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent()
        try {
            String json = new String(objectMapper.writeValueAsBytes([message: "Hello World"]))
            response.statusCode = 200
            response.body = json
        } catch (IOException e) {
            response.statusCode = 500
        }
        response
    }
}
