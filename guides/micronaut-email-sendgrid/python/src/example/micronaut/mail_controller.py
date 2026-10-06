from typing import Annotated

from micronaut.email import AsyncEmailSender, BodyType, Email
from micronaut.http import HttpResponse
from micronaut.http.annotation import Body, Controller, Post


@Controller("/mail")  # <1>
class MailController:
    def __init__(self, email_sender: AsyncEmailSender):  # <2>
        self.email_sender = email_sender

    @Post("/send")  # <3>
    async def send(self, to: Annotated[str, Body("to")]) -> HttpResponse:  # <4>
        email = (
            Email.builder()
            .to(to)
            .subject("Sending email with Twilio Sendgrid is Fun")
            .body(
                "and <em>easy</em> to do anywhere with <strong>Micronaut Email</strong>",
                BodyType.HTML,
            )
        )
        response = await self.email_sender.sendAsync(email)
        return (
            HttpResponse.unprocessableEntity()
            if response.getStatusCode() >= 400
            else HttpResponse.accepted()
        )  # <5>
