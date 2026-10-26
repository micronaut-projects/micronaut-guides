from jakarta.inject import Singleton
from micronaut.context.annotation import Primary

from .calculator import Calculator


@Primary  # <1>
@Singleton  # <2>
class BasicCalculator(Calculator):

    def add(self, a: int, b: int) -> int:
        return a + b
