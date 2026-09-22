import logging
from datetime import datetime
from typing import Annotated

from jakarta.inject import Named, Singleton
from java.time import Duration
from micronaut.scheduling import TaskExecutors, TaskScheduler

from .email_task import EmailTask
from .email_use_case import EmailUseCase

LOG = logging.getLogger(__name__)


def _current_time() -> str:
    now = datetime.now()
    return f"{now.day}/{now.month}/{now.year} {now:%I:%M:%S}"


@Singleton
class RegisterUseCase:
    def __init__(
        self,
        email_use_case: EmailUseCase,  # <1>
        task_scheduler: Annotated[TaskScheduler, Named(TaskExecutors.SCHEDULED)],  # <2>
    ):
        self.email_use_case = email_use_case
        self.task_scheduler = task_scheduler

    def register(self, email: str) -> None:
        LOG.info("saving %s at %s", email, _current_time())
        self._schedule_followup_email(email, "Welcome to the Micronaut framework")

    def _schedule_followup_email(self, email: str, message: str) -> None:
        task = EmailTask(self.email_use_case, email, message)  # <3>
        self.task_scheduler.schedule(Duration.ofMinutes(1), task)  # <4>
