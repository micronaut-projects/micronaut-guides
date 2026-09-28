from dataclasses import dataclass

from java.time import Month
from micronaut.serde.annotation import Serdeable


@Serdeable  # <1>
@dataclass
class News:
    month: Month
    headlines: list[str]
