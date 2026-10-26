from micronaut.http import MediaType
from micronaut.http.annotation import Controller, Get, Produces
from micronaut.security.annotation import Secured
from micronaut.security.rules import SecurityRule
from micronaut.views import View

from .contact_repository import ContactRepository


@Controller("/contacts")  # <1>
class ContactController:

    def __init__(self, contact_repository: ContactRepository):  # <2>
        self.contact_repository = contact_repository

    @Secured(SecurityRule.IS_ANONYMOUS)  # <3>
    @Produces(MediaType.TEXT_HTML)  # <4>
    @View("contacts/list.html")  # <5>
    @Get  # <6>
    def list(self) -> dict[str, object]:
        return {"contacts": self.contact_repository.find_all()}
