package example.micronaut.advanced.auth;

import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import example.micronaut.model.ViewModel;
import io.micronaut.views.View;

@Controller("/signin")
class SignInController {

    @View("signin")
    @Get(produces = {MediaType.TEXT_HTML}, consumes = {MediaType.TEXT_HTML})
    ViewModel index() {
        return new ViewModel("Sign In");
    }
}