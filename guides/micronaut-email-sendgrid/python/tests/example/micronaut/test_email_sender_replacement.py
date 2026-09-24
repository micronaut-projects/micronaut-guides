from jakarta.inject import Named, Singleton
from micronaut.context.annotation import Replaces, Requires
from micronaut.email import AsyncEmailSender, AsyncTransactionalEmailSender, Email
from micronaut.http import HttpStatus


class AcceptedResponse:
    def getStatusCode(self) -> int:
        return HttpStatus.ACCEPTED.getCode()


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

    async def sendAsync(self, email: Email, email_request=None) -> AcceptedResponse:
        self.emails.append(email)
        return AcceptedResponse()
