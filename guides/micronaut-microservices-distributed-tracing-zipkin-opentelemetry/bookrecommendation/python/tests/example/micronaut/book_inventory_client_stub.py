import asyncio
from typing import Annotated

from jakarta.inject import Singleton
from jakarta.validation.constraints import NotBlank
from micronaut.context.annotation import Requires
from micronaut.retry.annotation import Fallback

from example.micronaut.book_inventory_operations import BookInventoryOperations


@Requires(env="test")
@Fallback
@Singleton
class BookInventoryClientStub(BookInventoryOperations):

    async def stock(self, isbn: Annotated[str, NotBlank]) -> bool | None:
        await asyncio.sleep(0)
        if isbn == "1491950358":
            return True
        if isbn == "1680502395":
            return False
        return None
