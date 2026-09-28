from typing import Annotated

from jakarta.inject import Inject
from micronaut.http.annotation import Get

from .analytics_service import AnalyticsService
from .book_analytics import BookAnalytics


analytics_service: Annotated[AnalyticsService, Inject]


@Get("/analytics")  # <1>
def list_analytics() -> list[BookAnalytics]:
    return analytics_service.list_analytics()
