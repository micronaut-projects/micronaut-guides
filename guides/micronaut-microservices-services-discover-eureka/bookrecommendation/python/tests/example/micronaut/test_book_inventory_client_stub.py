import asyncio
from typing import Annotated

from jakarta.inject import Singleton
from jakarta.validation.constraints import NotBlank
from micronaut.context.annotation import Primary, Requires
from micronaut.retry.annotation import Fallback

from example.micronaut.book_inventory_operations import BookInventoryOperations


@Requires(env="test")  # <1>
@Fallback
@Singleton
class BookInventoryClientStub(BookInventoryOperations):

    in_flight = 0
    max_in_flight = 0

    async def stock(self, isbn: Annotated[str, NotBlank]) -> bool | None:
        self.in_flight += 1
        self.max_in_flight = max(self.max_in_flight, self.in_flight)
        try:
            await asyncio.sleep(0)
        finally:
            self.in_flight -= 1

        if isbn == "1491950358":
            return True  # <2>
        if isbn == "1680502395":
            return False  # <3>
        return None  # <4>


@Requires(property="spec.name", value="BookControllerErrorTest")
@Primary
@Singleton
class BookInventoryClientErrorStub(BookInventoryOperations):

    async def stock(self, isbn: Annotated[str, NotBlank]) -> bool | None:
        raise RuntimeError("inventory unavailable")
