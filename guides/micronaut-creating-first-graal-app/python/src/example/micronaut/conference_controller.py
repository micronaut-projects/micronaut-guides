from micronaut.http.annotation import Controller, Get

from .conference import Conference
from .conference_service import ConferenceService


@Controller("/conferences")  # <1>
class ConferenceController:
    def __init__(self, conference_service: ConferenceService):  # <2>
        self.conference_service = conference_service

    @Get("/random")  # <3>
    def random_conf(self) -> Conference:  # <4>
        return self.conference_service.random_conf()
