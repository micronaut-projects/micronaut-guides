package example.micronaut;

import io.micronaut.core.annotation.Nullable;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.authentication.Authentication;
import io.micronaut.security.x509.X509Authentication;

import static io.micronaut.http.MediaType.TEXT_PLAIN;
import static io.micronaut.security.rules.SecurityRule.IS_ANONYMOUS;

@Controller
class HelloController {

    @Secured(IS_ANONYMOUS) // <1>
    @Get(produces = TEXT_PLAIN) // <2>
    String hello(@Nullable X509Authentication x509Authentication, // <3>
                 @Nullable Authentication authentication) { // <4>
        if (x509Authentication == null && authentication == null) {
            return "Hello unknown!"; // <5>
        }
        if (x509Authentication == null) {
            return "ERROR: Authentication is present but not X509Authentication"; // <6>
        }
        if (x509Authentication != authentication) {
            return "ERROR: Authentication and X509Authentication should be the same instance"; // <7>
        }
        return "Hello " +
                x509Authentication.getName() +
                " (X.509 cert issued by " + x509Authentication.getCertificate().getIssuerX500Principal().getName() + ')'; // <8>
    }
}
