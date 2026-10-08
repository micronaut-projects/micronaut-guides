from dataclasses import dataclass
from decimal import Decimal

from micronaut.serde.annotation import Serdeable


@Serdeable
@dataclass
class SalarySummaryDto:
    minSalary: Decimal | None
    maxSalary: Decimal | None
    averageSalary: Decimal | None
    employeeCount: int
