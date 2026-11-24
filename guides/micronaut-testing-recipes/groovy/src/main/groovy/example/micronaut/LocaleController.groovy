package example.micronaut

import groovy.transform.CompileStatic
import io.micronaut.http.HttpResponse
import io.micronaut.http.MediaType
import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Consumes
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Post
import io.micronaut.http.cookie.Cookie
import io.micronaut.security.annotation.Secured
import io.micronaut.security.rules.SecurityRule
import jakarta.validation.constraints.NotBlank

@CompileStatic
@Controller("/locale") // <1>
class LocaleController {

    private static final URI HOME = URI.create("/")
    private static final String COOKIE_NAME = "locale"

    @Secured(SecurityRule.IS_ANONYMOUS) // <2>
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED) // <3>
    @Post // <4>
    HttpResponse<?> changeLocale(@Body("locale") @NotBlank String locale) {
        HttpResponse.seeOther(HOME)
                .cookie(Cookie.of(COOKIE_NAME, locale)) // <5>
    }
}
