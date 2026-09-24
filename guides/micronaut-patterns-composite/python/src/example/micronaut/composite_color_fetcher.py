from jakarta.inject import Singleton
from micronaut.context.annotation import Primary
from micronaut.http import HttpRequest

from .color_fetcher import ColorFetcher


@Primary  # <1>
@Singleton  # <2>
class CompositeColorFetcher(ColorFetcher):
    def __init__(self, color_fetcher_list: list[ColorFetcher]):  # <3>
        self.color_fetcher_list = color_fetcher_list

    def favourite_color(self, request: HttpRequest) -> str | None:
        for color_fetcher in sorted(
            self.color_fetcher_list, key=lambda fetcher: fetcher.getOrder()
        ):
            color = color_fetcher.favourite_color(request)
            if color is not None:
                return color
        return None
