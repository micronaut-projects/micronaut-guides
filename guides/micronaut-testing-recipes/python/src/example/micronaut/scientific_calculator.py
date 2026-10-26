from jakarta.inject import Singleton

from .calculator import Calculator


@Singleton  # <1>
class ScientificCalculator(Calculator):

    def add(self, a: int, b: int) -> int:
        result = a + b
        if not -(2**31) <= result < 2**31:
            raise OverflowError("integer overflow")
        return result
