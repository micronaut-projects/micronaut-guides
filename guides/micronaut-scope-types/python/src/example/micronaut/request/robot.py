from uuid import uuid4

from micronaut.runtime.http.scope import RequestScope


@RequestScope  # <1>
class Robot:
    def __init__(self):
        self._serial_number = str(uuid4())

    @property
    def serial_number(self) -> str:
        return self._serial_number
