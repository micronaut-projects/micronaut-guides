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

        var username = "unknown!" // <5>
        if (x509Authentication == null) {
            if (authentication != null) {
                return "ERROR: Authentication is present but not X509Authentication" // <6>
            }
        } else {
            if (x509Authentication !== authentication) {
                return "ERROR: Authentication and X509Authentication should be the same instance" // <7>
            }
            val clientCertificate = x509Authentication.certificate
            val issuer = clientCertificate.issuerDN.name
            username = "${x509Authentication.name} (X.509 cert issued by ${issuer})" // <8>
        }

        return "Hello ${username}"
    }
}
