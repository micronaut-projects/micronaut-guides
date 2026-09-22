from typing import Annotated

from jakarta.inject import Singleton
from jakarta.validation.constraints import NotBlank
from micronaut.context.annotation import Requires
from micronaut.core.async_.annotation import SingleResult
from micronaut.retry.annotation import Fallback
from org.reactivestreams import Publisher
from reactor.core.publisher import Mono

from example.micronaut.book_inventory_operations import BookInventoryOperations


@Requires(env="test")
@Fallback
@Singleton
class BookInventoryClientStub(BookInventoryOperations):

    @SingleResult
    def stock(self, isbn: Annotated[str, NotBlank]) -> Publisher[bool]:
        if isbn == "1491950358":
            return Mono.just(True)
        if isbn == "1680502395":
            return Mono.just(False)
        return Mono.empty()
