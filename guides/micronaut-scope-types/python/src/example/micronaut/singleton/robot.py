from uuid import uuid4

from jakarta.inject import Singleton


@Singleton  # <1>
class Robot:
    def __init__(self):
        self._serial_number = str(uuid4())

    @property
    def serial_number(self) -> str:
        return self._serial_number
