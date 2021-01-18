package example.micronaut;

import io.micronaut.context.annotation.Requires;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Produces;
import io.micronaut.http.server.exceptions.ExceptionHandler;
import javax.inject.Singleton;

@Produces
@Singleton // <1>
@Requires(classes = {OutOfStockException.class, ExceptionHandler.class})  // <2>
public class OutOfStockExceptionHandler implements ExceptionHandler<OutOfStockException, HttpResponse> { // <3>

    @Override
    public HttpResponse handle(HttpRequest request, OutOfStockException exception) {
        return HttpResponse.ok(0); // <4>
    }
}
