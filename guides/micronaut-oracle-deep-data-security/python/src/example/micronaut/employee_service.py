from decimal import ROUND_HALF_UP, Decimal

from jakarta.inject import Singleton
from micronaut.security.authentication import Authentication

from .employee_repository import EmployeeRepository
from .salary_aggregates import SalaryAggregates
from .salary_summary_dto import SalarySummaryDto

CENTS = Decimal("0.01")


@Singleton  # <1>
class EmployeeService:
    def __init__(self, employee_repository: EmployeeRepository):  # <2>
        self.employee_repository = employee_repository

    def employee_model(self, authentication: Authentication | None) -> dict:
        model: dict = {}
        if authentication is not None:  # <3>
            model["employees"] = self.employee_repository.findAll()  # <4>
            model["salarySummary"] = salary_summary(self.employee_repository.getSalaryAggregates())  # <5>
        return model


def salary_summary(aggregates: SalaryAggregates) -> SalarySummaryDto:
    employee_count = 0 if aggregates.count is None else int(aggregates.count)
    average_salary = None
    if aggregates.sum is not None and employee_count > 0:
        average_salary = (Decimal(aggregates.sum) / employee_count).quantize(CENTS, rounding=ROUND_HALF_UP)
    return SalarySummaryDto(
        minSalary=aggregates.min,
        maxSalary=aggregates.max,
        averageSalary=average_salary,
        employeeCount=employee_count,
    )
