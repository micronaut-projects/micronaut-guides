from decimal import Decimal

from example.micronaut.employee_repository import EmployeeRepository


def test_find_all(application_context, run_as):
    repository = application_context[EmployeeRepository]
    with run_as("sergio"):  # <1>
        assert len(list(repository.findAll())) == 5


def test_get_salary_aggregates(application_context, run_as):
    repository = application_context[EmployeeRepository]
    with run_as("sergio"):
        aggregates = repository.getSalaryAggregates()  # <2>

    assert aggregates.min == Decimal("6900")
    assert aggregates.max == Decimal("13000")
    assert aggregates.sum == Decimal("49130")
    assert aggregates.count == 5
