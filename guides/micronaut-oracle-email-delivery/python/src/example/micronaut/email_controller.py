from datetime import datetime

from micronaut.email import Attachment, BodyType, Email, EmailSender
from micronaut.email.template import TemplateBody
from micronaut.http import MediaType
from micronaut.http.annotation import Consumes, Controller, Post, Produces
from micronaut.http.multipart import CompletedFileUpload
from micronaut.scheduling import TaskExecutors
from micronaut.scheduling.annotation import ExecuteOn
from micronaut.views import ModelAndView


@ExecuteOn(TaskExecutors.BLOCKING)  # <1>
@Controller("/email")  # <2>
class EmailController:
    def __init__(self, email_sender: EmailSender):  # <3>
        self.email_sender = email_sender

    @Produces(MediaType.TEXT_PLAIN)  # <4>
    @Post("/basic")
    def index(self) -> str:
        self.email_sender.send(
            Email.builder()
            .to("basic@domain.com")
            .subject(f"Micronaut Email Basic Test: {datetime.now()}")
            .body("Basic email")
        )  # <5>
        return "Email sent."

    @Produces(MediaType.TEXT_PLAIN)  # <4>
    @Post("/template/{name}")
    def template(self, name: str) -> str:
        self.email_sender.send(
            Email.builder()
            .to("template@domain.com")
            .subject(f"Micronaut Email Template Test: {datetime.now()}")
            .body(TemplateBody(BodyType.HTML, ModelAndView("email", {"name": name})))
        )  # <6>
        return "Email sent."

    @Consumes(MediaType.MULTIPART_FORM_DATA)  # <7>
    @Produces(MediaType.TEXT_PLAIN)  # <4>
    @Post("/attachment")
    def attachment(self, file: CompletedFileUpload) -> str:
        self.email_sender.send(
            Email.builder()
            .to("attachment@domain.com")
            .subject(f"Micronaut Email Attachment Test: {datetime.now()}")
            .body("Attachment email")
            .attachment(
                Attachment.builder()
                .filename(file.getFilename())
                .contentType(
                    file.getContentType()
                    .orElse(MediaType.APPLICATION_OCTET_STREAM_TYPE)
                    .toString()
                )
                .content(file.getBytes())
                .build()
            )
        )  # <8>
        return "Email sent."
