import java
from jakarta.inject import Named, Singleton
from micronaut.context.annotation import Replaces, Requires
from micronaut.email import AsyncEmailSender, AsyncTransactionalEmailSender, Email
from micronaut.http import HttpStatus
from reactor.core.publisher import Mono

Response = java.type("com.sendgrid.Response")


@Requires(property="spec.name", value="MailControllerTest")  # <1>
@Singleton
@Replaces(AsyncEmailSender)
@Named("sendgrid")
class EmailSenderReplacement(AsyncTransactionalEmailSender):
    NAME = "sendgrid"

    def __init__(self):
        self.emails = []

    def getName(self) -> str:
        return self.NAME

    def sendAsync(self, email: Email, email_request=None):
        self.emails.append(email)
        response = Response()
        response.setStatusCode(HttpStatus.ACCEPTED.getCode())
        return Mono.just(response)
