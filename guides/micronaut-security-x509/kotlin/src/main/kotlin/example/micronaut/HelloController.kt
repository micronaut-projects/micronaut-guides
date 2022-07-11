package example.micronaut

import io.micronaut.http.MediaType.TEXT_PLAIN
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.security.annotation.Secured
import io.micronaut.security.authentication.Authentication
import io.micronaut.security.rules.SecurityRule.IS_ANONYMOUS
import io.micronaut.security.x509.X509Authentication

@Controller
class HelloController {

    @Secured(IS_ANONYMOUS) // <1>
    @Get(produces = [TEXT_PLAIN]) // <2>
    fun hello(x509Authentication: X509Authentication?,  // <3>
              authentication: Authentication?): String { // <4>
        if (x509Authentication == null && authentication == null) {
            return "Hello unknown!" // <5>
        }
        if (x509Authentication == null) {
            return "ERROR: Authentication is present but not X509Authentication" // <6>
        }
        return if (x509Authentication !== authentication) {
            "ERROR: Authentication and X509Authentication should be the same instance" // <7>
        } else "Hello ${x509Authentication.name} (X.509 cert issued by ${x509Authentication.certificate.issuerX500Principal.name})" // <8>

    }
}
