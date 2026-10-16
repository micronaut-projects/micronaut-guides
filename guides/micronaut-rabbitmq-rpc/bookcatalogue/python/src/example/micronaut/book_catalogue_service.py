from micronaut.rabbitmq.annotation import Queue, RabbitListener

from .book import Book


@RabbitListener  # <1>
class BookCatalogueService:

    @Queue("catalogue")  # <2>
    def list_books(self) -> list[Book]:
        building_microservices = Book("1491950358", "Building Microservices")
        release_it = Book("1680502395", "Release It!")
        ci_delivery = Book("0321601912", "Continuous Delivery")

        return [building_microservices, release_it, ci_delivery]
