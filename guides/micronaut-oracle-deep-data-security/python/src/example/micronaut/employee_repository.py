from java.lang import Long
from micronaut.data.annotation import Query
from micronaut.data.jdbc.annotation import JdbcRepository
from micronaut.data.model.query.builder.sql import Dialect
from micronaut.data.repository import CrudRepository
from micronaut.security.annotation import RunAs

from .employee_entity import EmployeeEntity
from .salary_aggregates import SalaryAggregates


@JdbcRepository(dialect=Dialect.ORACLE)  # <1>
class EmployeeRepository(CrudRepository[EmployeeEntity, Long]):  # <2>

    @RunAs("ORACLE_DATA_ROLE_COMPENSATION_ANALYST")  # <3>
    @Query("SELECT MIN(salary) AS min, MAX(salary) AS max, SUM(salary) AS sum, COUNT(*) AS count FROM hr.employees")  # <4>
    def getSalaryAggregates(self) -> SalaryAggregates: ...
