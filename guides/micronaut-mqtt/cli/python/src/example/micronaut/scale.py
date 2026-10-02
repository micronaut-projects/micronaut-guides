from enum import Enum


class Scale(Enum):
    FAHRENHEIT = "Fahrenheit"
    CELSIUS = "Celsius"

    @classmethod
    def of(cls, name: str) -> "Scale":
        return cls._value2member_map_.get(name, cls.CELSIUS)

    @classmethod
    def candidates(cls) -> set[str]:
        return {scale.value for scale in cls}
