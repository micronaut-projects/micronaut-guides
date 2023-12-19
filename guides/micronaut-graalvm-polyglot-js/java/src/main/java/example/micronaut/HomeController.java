package com.example;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Produces;
import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.Value;

@Controller
public class HomeController {

    @Produces(MediaType.TEXT_PLAIN)
    @Get
    HttpResponse<String> index() {
        try (Context context = Context.create()) {
            Value f = context.eval("js", "x => x + 1");
            if (f.canExecute()) {
                return HttpResponse.ok("" + f.execute(41).asInt());
            }
        }
        return HttpResponse.serverError();
    }
}
