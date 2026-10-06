from jakarta.inject import Singleton
from micronaut.http import HttpRequest

from .color_fetcher import ColorFetcher


@Singleton  # <1>
class HttpHeaderColorFetcher(ColorFetcher):
    def favourite_color(self, request: HttpRequest) -> str | None:
        return request.getHeaders().get("color")

    def getOrder(self) -> int:  # <2>
        return 10
