from micronaut.http import MediaType
from micronaut.http.annotation import Get
from micronaut.security.annotation import Secured
from micronaut.security.authentication import Authentication
from micronaut.security.rules import SecurityRule
from micronaut.security.x509 import X509Authentication


@Secured(SecurityRule.IS_ANONYMOUS)  # <1>
@Get(value="/", produces=MediaType.TEXT_PLAIN)  # <2>
def hello(  # <3> <4>
    x509_authentication: X509Authentication = None,
    authentication: Authentication = None,
) -> str:
    if x509_authentication is None and authentication is None:
        return "Hello unknown!"  # <5>
    if x509_authentication is None:
        return "ERROR: Authentication is present but not X509Authentication"  # <6>
    if x509_authentication is not authentication:
        return "ERROR: Authentication and X509Authentication should be the same instance"  # <7>
    issuer = x509_authentication.getCertificate().getIssuerX500Principal().getName()
    return f"Hello {x509_authentication.getName()} (X.509 cert issued by {issuer})"  # <8>
