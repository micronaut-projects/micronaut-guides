package example.micronaut;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Produces;
import io.micronaut.views.ModelAndView;

import java.util.Collections;
import java.util.Map;

@Controller // <1>
class MessageController {

    @Produces(value = {MediaType.TEXT_HTML, MediaType.APPLICATION_JSON}) // <2>
    @Get // <3>
    HttpResponse<?> index(HttpRequest<?> request) { // <4>
        Map<String, Object> model = Collections.singletonMap("message", "Hello World");
        Object body = accepts(request, MediaType.TEXT_HTML_TYPE)
                ? new ModelAndView<>("message.html", model)
                : model;
        return HttpResponse.ok(body);
    }

    private static boolean accepts(HttpRequest<?> request, MediaType mediaType) {
        return request.getHeaders()
                .accept()
                .stream()
                .anyMatch(it -> it.getName().contains(mediaType));
    }
}