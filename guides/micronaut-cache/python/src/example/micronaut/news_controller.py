from typing import Annotated

from jakarta.inject import Inject
from java.time import Month
from micronaut.http.annotation import Get

from .news import News
from .news_service import NewsService


news_service: Annotated[NewsService, Inject]


@Get("/{month}")  # <1>
def index(month: Month) -> News:
    return News(month, news_service.headlines(month))
