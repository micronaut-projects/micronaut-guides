import logging
from datetime import datetime

from jakarta.inject import Singleton
from micronaut.scheduling.annotation import Scheduled

LOG = logging.getLogger(__name__)  # <2>


def _current_time() -> str:
    now = datetime.now()
    return f"{now.day}/{now.month}/{now.year} {now:%I:%M:%S}"


@Singleton  # <1>
class HelloWorldJob:
    @Scheduled(fixedDelay="10s")  # <3>
    def execute_every_ten(self) -> None:
        LOG.info("Simple Job every 10 seconds: %s", _current_time())

    @Scheduled(fixedDelay="45s", initialDelay="5s")  # <4>
    def execute_every_forty_five(self) -> None:
        LOG.info("Simple Job every 45 seconds: %s", _current_time())
