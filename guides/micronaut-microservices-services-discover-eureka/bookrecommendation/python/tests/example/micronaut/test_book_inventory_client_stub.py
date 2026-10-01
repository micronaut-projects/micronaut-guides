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

    async def stock(self, isbn: Annotated[str, NotBlank]) -> bool | None:
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
