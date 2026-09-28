import logging
from datetime import datetime

from jakarta.inject import Singleton

LOG = logging.getLogger(__name__)


def _current_time() -> str:
    now = datetime.now()
    return f"{now.day}/{now.month}/{now.year} {now:%I:%M:%S}"


@Singleton
class EmailUseCase:
    def send(self, user: str, message: str) -> None:
        LOG.info("Sending email to %s: %s at %s", user, message, _current_time())
