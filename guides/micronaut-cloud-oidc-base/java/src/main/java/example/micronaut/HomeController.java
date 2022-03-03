package example.micronaut;

import io.micronaut.core.util.CollectionUtils;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.security.annotation.Secured;
import io.micronaut.views.View;

import java.util.HashMap;
import java.util.Map;

import static io.micronaut.security.rules.SecurityRule.IS_ANONYMOUS;
import static io.micronaut.security.rules.SecurityRule.IS_AUTHENTICATED;

@Controller // <1>
public class HomeController {

    @Secured(IS_ANONYMOUS) // <2>
    @View("home") // <3>
    @Get // <4>
    public Map<String, Object> index() {
        return new HashMap<>();
    }

    @Secured(IS_AUTHENTICATED) // <5>
    @Get("/secure") // <6>
    public Map<String, Object> secured() {
        return CollectionUtils.mapOf("secured", true); // <7>
    }
}
