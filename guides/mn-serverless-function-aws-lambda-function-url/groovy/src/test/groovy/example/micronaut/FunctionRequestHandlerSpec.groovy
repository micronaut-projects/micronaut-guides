package example.micronaut

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent
import spock.lang.AutoCleanup
import spock.lang.Shared
import spock.lang.Specification

class FunctionRequestHandlerSpec extends Specification {

    @AutoCleanup
    @Shared
    FunctionRequestHandler handler = new FunctionRequestHandler()

    void "test Handler"() {
        given:
        APIGatewayProxyRequestEvent request = new APIGatewayProxyRequestEvent()
        request.httpMethod = "GET"
        request.path = "/"

        when:
        APIGatewayProxyResponseEvent response = handler.execute(request)

        then:
        response.getStatusCode().intValue() == 200
        response.body == '{"message":"Hello World"}'
    }
}
