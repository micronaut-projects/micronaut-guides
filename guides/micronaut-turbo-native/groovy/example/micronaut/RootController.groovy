package example.micronaut

import io.micronaut.core.annotation.Nullable
import io.micronaut.http.MediaType
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import example.micronaut.model.ViewModel
import io.micronaut.views.View
import java.security.Principal

@Controller
class RootController {

    @View("index")
    @Get(produces = [MediaType.TEXT_HTML], consumes = [MediaType.TEXT_HTML])
    ViewModel index(@Nullable Principal principal) {
        new ViewModel("Turbo Native Demo", "index", null, principal)
    }
}