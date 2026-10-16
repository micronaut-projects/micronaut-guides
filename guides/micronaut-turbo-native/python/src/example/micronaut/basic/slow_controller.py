from time import sleep

from micronaut.http import MediaType
from micronaut.http.annotation import Get, Produces
from micronaut.scheduling import TaskExecutors
from micronaut.scheduling.annotation import ExecuteOn
from micronaut.views import View

from ..model.view_model import ViewModel


@ExecuteOn(TaskExecutors.BLOCKING)
@View("slow")
@Get("/slow")
@Produces(MediaType.TEXT_HTML)
def index() -> dict:
    sleep(3)
    return ViewModel("Slow-loading Page").to_dict()
