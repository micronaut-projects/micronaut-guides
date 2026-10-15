from micronaut.core.order import Ordered
from micronaut.http import HttpRequest
from micronaut.http.annotation import RequestFilter, ServerFilter
from micronaut.http.filter import ServerFilterPhase
from micronaut.http.util import HttpHeadersUtil
from org.slf4j import LoggerFactory


@ServerFilter("/**")  # <1>
class LoggingHeadersFilter(Ordered):
    LOG = LoggerFactory.getLogger("example.micronaut.LoggingHeadersFilter")

    @RequestFilter  # <2>
    def filterRequest(self, request: HttpRequest) -> None:
        HttpHeadersUtil.trace(self.LOG, request.getHeaders())

    def getOrder(self) -> int:  # <3>
        return ServerFilterPhase.FIRST.order()
