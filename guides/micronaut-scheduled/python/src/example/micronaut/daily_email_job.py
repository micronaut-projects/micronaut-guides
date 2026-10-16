from jakarta.inject import Singleton
from micronaut.scheduling.annotation import Scheduled

from .email_use_case import EmailUseCase


@Singleton  # <1>
class DailyEmailJob:
    def __init__(self, email_use_case: EmailUseCase):  # <2>
        self.email_use_case = email_use_case

    @Scheduled(cron="0 30 4 1/1 * ?")  # <3>
    def execute(self) -> None:
        self.email_use_case.send("john.doe@micronaut.example", "Test Message")  # <4>
