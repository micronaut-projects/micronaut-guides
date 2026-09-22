from micronaut.http.annotation import Get
from micronaut.security.annotation import Secured
from micronaut.security.rules import SecurityRule

from .book import Book


@Get("/books")
@Secured(SecurityRule.IS_AUTHENTICATED)  # <1>
def index() -> list[Book]:
    building_microservices = Book("1491950358", "Building Microservices")
    release_it = Book("1680502395", "Release It!")
    continuous_delivery = Book("0321601912", "Continuous Delivery:")
    return [building_microservices, release_it, continuous_delivery]
