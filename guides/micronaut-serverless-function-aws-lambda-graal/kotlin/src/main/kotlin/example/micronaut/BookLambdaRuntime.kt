package example.micronaut

import com.amazonaws.services.lambda.runtime.RequestHandler
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent

class BookLambdaRuntime :
    AbstractMicronautLambdaRuntime<APIGatewayProxyRequestEvent?, APIGatewayProxyResponseEvent?, Book?, BookSaved?>() {
    protected fun createRequestHandler(vararg args: String?): RequestHandler<Book, BookSaved>? {
        return BookRequestHandler()
    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            try {
                BookLambdaRuntime().run(args)
            } catch (e: MalformedURLException) {
                e.printStackTrace()
            }
        }
    }
}
