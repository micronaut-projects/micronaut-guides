from micronaut.http import MediaType
from micronaut.http.annotation import Controller, Get, Produces
from micronaut.scheduling import TaskExecutors
from micronaut.scheduling.annotation import ExecuteOn
from micronaut.security.annotation import Secured
from micronaut.security.authentication import Authentication
from micronaut.security.rules import SecurityRule
from micronaut.views import View

from .employee_service import EmployeeService


@Controller  # <1>
class HomeController:
    def __init__(self, employee_service: EmployeeService):  # <2>
        self.employee_service = employee_service

    @Secured(SecurityRule.IS_ANONYMOUS)  # <3>
    @Produces(MediaType.TEXT_HTML)  # <4>
    @View("home")  # <5>
    @Get  # <6>
    @ExecuteOn(TaskExecutors.BLOCKING)  # <7>
    def index(self, authentication: Authentication | None) -> dict:  # <8>
        return self.employee_service.employee_model(authentication)
