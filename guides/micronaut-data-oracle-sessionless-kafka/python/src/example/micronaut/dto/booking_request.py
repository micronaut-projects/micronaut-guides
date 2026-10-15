from dataclasses import dataclass
from typing import Annotated

from jakarta.validation.constraints import NotBlank, Pattern
from micronaut.serde.annotation import Serdeable


@Serdeable  # <1>
@dataclass
class BookingRequest:
    seatId: Annotated[str, NotBlank]
    paymentToken: Annotated[str, NotBlank, Pattern(regexp="demo-approve|demo-decline")]
