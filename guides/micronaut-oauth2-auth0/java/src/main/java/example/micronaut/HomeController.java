package example.micronaut;

import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;
import io.micronaut.views.View;

import java.util.HashMap;
import java.util.Map;

@Controller // <1>
public class HomeController {

    @Secured(SecurityRule.IS_ANONYMOUS) // <2>
    @View("home") // <3>
    @Get // <4>
    public HttpResponse<Map<String, Object>> index(@Nullable Authentication authentication) {

        HashMap<String, Object> variables = new HashMap<>();
        if (authentication != null) {
            variables.putAll(authentication.getAttributes());
        }

        return HttpResponse.ok(variables);
    }
}
