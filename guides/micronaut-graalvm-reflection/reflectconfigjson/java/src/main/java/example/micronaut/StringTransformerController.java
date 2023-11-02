package example.micronaut;

import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Produces;
import io.micronaut.http.annotation.QueryValue;

@Controller("/transformer") // <1>
public class StringTransformerController {

    private final StringTransformer transformer;

    public StringTransformerController(StringTransformer transformer) {  // <2>
        this.transformer = transformer;
    }

    @Produces(MediaType.TEXT_PLAIN) // <3>
    @Get("/capitalize{?q}") // <4>
    String capitalize(@QueryValue String q) { // <5>
        String className = "example.micronaut.StringCapitalizer";
        String methodName = "capitalize";
        return transformer.transform(q, className, methodName);
    }

    @Produces(MediaType.TEXT_PLAIN) // <3>
    @Get("/reverse{?q}") // <4>
    String reverse(@QueryValue String q) { // <5>
        String className = "example.micronaut.StringReverser";
        String methodName = "reverse";
        return transformer.transform(q, className, methodName);
    }
}
