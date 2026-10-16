from micronaut.http import HttpRequest, MediaType
from micronaut.http.annotation import Controller, Get, Produces

from .color_fetcher import ColorFetcher


@Controller("/color")  # <1>
class ColorController:
    def __init__(self, color_fetcher: ColorFetcher):  # <2>
        self.color_fetcher = color_fetcher

    @Produces(MediaType.TEXT_PLAIN)  # <3>
    @Get("/mint")  # <4>
    def mint(self, request: HttpRequest) -> str | None:  # <5>
        return self.color_fetcher.favourite_color(request)

    @Produces(MediaType.TEXT_PLAIN)  # <3>
    @Get
    def index(self, request: HttpRequest) -> str | None:  # <5>
        return self.color_fetcher.favourite_color(request)
