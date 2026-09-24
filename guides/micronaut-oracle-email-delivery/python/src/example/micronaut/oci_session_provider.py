from typing import Annotated

from jakarta.inject import Singleton
from jakarta.mail import Authenticator, PasswordAuthentication, Session
from micronaut.context.annotation import Property
from micronaut.email.javamail.sender import MailPropertiesProvider, SessionProvider


@Singleton  # <1>
class OciSessionProvider(SessionProvider):
    def __init__(
        self,
        provider: MailPropertiesProvider,
        user: Annotated[str, Property(name="smtp.user")],  # <2>
        password: Annotated[str, Property(name="smtp.password")],  # <2>
    ):
        self.properties = provider.mailProperties()
        self.user = user
        self.password = password

    def session(self) -> Session:
        user = self.user
        password = self.password

        class OciAuthenticator(Authenticator):
            def getPasswordAuthentication(self) -> PasswordAuthentication:
                return PasswordAuthentication(user, password)  # <3>

        return Session.getInstance(self.properties, OciAuthenticator())
