from dataclasses import dataclass
from decimal import Decimal
from typing import Annotated

from java.lang import Long
from micronaut.data.annotation import Id, MappedEntity, MappedProperty
from micronaut.serde.annotation import Serdeable


@Serdeable
@MappedEntity(value="employees", schema="hr")  # <1>
@dataclass
class EmployeeEntity:
    employeeId: Annotated[Long | None, Id, MappedProperty("employee_id")] = None  # <2>
    firstName: Annotated[str | None, MappedProperty("first_name")] = None
    lastName: Annotated[str | None, MappedProperty("last_name")] = None
    email: str | None = None
    manager: str | None = None
    ssn: str | None = None
    salary: Decimal | None = None
    phone: str | None = None
