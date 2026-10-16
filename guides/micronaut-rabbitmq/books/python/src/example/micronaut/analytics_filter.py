from micronaut.http import HttpStatus, MutableHttpResponse
from micronaut.http.annotation import ResponseFilter, ServerFilter
from micronaut.scheduling import TaskExecutors
from micronaut.scheduling.annotation import ExecuteOn

from .analytics_client import AnalyticsClient


@ServerFilter("/books/?*")  # <1>
class AnalyticsFilter:  # <2>

    def __init__(self, analytics_client: AnalyticsClient):  # <3>
        self.analytics_client = analytics_client

    @ResponseFilter  # <4>
    @ExecuteOn(TaskExecutors.BLOCKING)  # <5>
    def filter_response(self, response: MutableHttpResponse) -> None:
        if response.status() != HttpStatus.OK:
            return
        book = response.getBody().orElse(None)  # <6>
        if book is not None:
            self.analytics_client.update_analytics(book)  # <7>
