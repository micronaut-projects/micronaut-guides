package example.micronaut
import io.micronaut.function.aws.MicronautRequestHandler
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import io.micronaut.json.JsonMapper;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent
import jakarta.inject.Inject
class FunctionRequestHandler : MicronautRequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent>() {
    @Inject
    lateinit var objectMapper: JsonMapper

    override fun execute(input: APIGatewayProxyRequestEvent): APIGatewayProxyResponseEvent {
        val response = APIGatewayProxyResponseEvent()
        try {
            val json = String(objectMapper.writeValueAsBytes(mapOf("message" to "Hello World")))
            response.statusCode = 200
            response.body = json
        } catch (e: IOException) {
            response.statusCode = 500
        }
        return response
    }
}
