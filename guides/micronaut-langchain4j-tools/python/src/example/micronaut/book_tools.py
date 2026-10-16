from typing import Annotated

from dev.langchain4j.agent.tool import P, Tool
from jakarta.inject import Singleton
from micronaut.core.annotation import AllowsReflection

AVAILABLE_COPIES = {  # <1>
    "dune": 3,
    "foundation": 1,
    "neuromancer": 0,
}


@Singleton  # <2>
@AllowsReflection  # <3>
class BookTools:

    @Tool(name="availableCopies", value="Returns the number of copies of a book that the library can lend right now")  # <4>
    def available_copies(self, title: Annotated[str, P(name="title", value="The title of the book")]) -> int:  # <5>
        return AVAILABLE_COPIES.get(title.strip().lower(), 0)
