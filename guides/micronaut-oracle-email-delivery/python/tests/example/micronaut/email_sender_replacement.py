from jakarta.inject import Named, Singleton
from micronaut.context.annotation import Replaces, Requires
from micronaut.email import EmailSender, TransactionalEmailSender


@Requires(property="spec.name", value="EmailControllerTest")  # <1>
@Singleton
@Replaces(value=EmailSender, named="javaxmail")
@Named("javaxmail")
class EmailSenderReplacement(EmailSender, TransactionalEmailSender):
    def __init__(self):
        self.emails = []

    def getName(self) -> str:
        return "test"

    def send(self, email, email_request=None):
        if hasattr(email, "build"):
            email = email.build()
        self.emails.append(email)
        return None
