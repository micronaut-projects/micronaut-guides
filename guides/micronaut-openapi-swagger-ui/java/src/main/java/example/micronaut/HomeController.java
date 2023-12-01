package example.micronaut;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.uri.UriBuilder;
import io.swagger.v3.oas.annotations.Hidden;
import java.net.URI;

@Controller // <1>
class HomeController {

    private final static URI SWAGGER_UI = UriBuilder.of("/swagger-ui").path("index.html").build();

    @Get // <2>
    @Hidden // <3>
    HttpResponse<?> home() {
        return HttpResponse.seeOther(SWAGGER_UI);
    }
}
