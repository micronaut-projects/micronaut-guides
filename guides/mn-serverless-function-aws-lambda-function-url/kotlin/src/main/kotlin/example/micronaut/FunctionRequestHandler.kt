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
