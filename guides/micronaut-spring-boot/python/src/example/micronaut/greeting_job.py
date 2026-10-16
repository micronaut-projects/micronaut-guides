import logging

from org.springframework.scheduling.annotation import Scheduled
from org.springframework.stereotype import Component

from .greeting_service import GreetingService

LOG = logging.getLogger(__name__)


@Component  # <1>
class GreetingJob:
    def __init__(self, greeting_service: GreetingService):  # <2>
        self.greeting_service = greeting_service

    @Scheduled(fixedDelayString="30s")  # <3>
    def print_last_greeting(self) -> None:
        last_greeting = self.greeting_service.get_last_greeting()
        if last_greeting is not None:
            LOG.info("Last Greeting was = %s", last_greeting.content)
