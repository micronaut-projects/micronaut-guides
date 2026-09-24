from jakarta.inject import Singleton
from micronaut.http import HttpRequest

from .color_fetcher import ColorFetcher


@Singleton  # <1>
class PathColorFetcher(ColorFetcher):
    COLORS = (
        "Red",
        "Blue",
        "Green",
        "Orange",
        "White",
        "Black",
        "Yellow",
        "Purple",
        "Silver",
        "Brown",
        "Gray",
        "Pink",
        "Olive",
        "Maroon",
        "Violet",
        "Charcoal",
        "Magenta",
        "Bronze",
        "Cream",
        "Gold",
        "Tan",
        "Teal",
        "Mustard",
        "Navy Blue",
        "Coral",
        "Burgundy",
        "Lavender",
        "Mauve",
        "Peach",
        "Rust",
        "Indigo",
        "Ruby",
        "Clay",
        "Cyan",
        "Azure",
        "Beige",
        "Turquoise",
        "Amber",
        "Mint",
    )

    def favourite_color(self, request: HttpRequest) -> str | None:
        path = str(request.getPath()).lower()
        for color in self.COLORS:
            normalized = color.lower()
            if normalized in path:
                return normalized
        return None

    def getOrder(self) -> int:  # <2>
        return 20
