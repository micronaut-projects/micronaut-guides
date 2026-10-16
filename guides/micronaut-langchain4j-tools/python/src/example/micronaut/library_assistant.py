from abc import ABC, abstractmethod

from dev.langchain4j.service import SystemMessage
from micronaut.core.annotation import AllowsReflection
from micronaut.langchain4j.annotation import AiService

from .book_tools import BookTools


@AiService(tools=[BookTools])  # <1>
@AllowsReflection  # <2>
class LibraryAssistant(ABC):

    @SystemMessage("You are the assistant of a public library. Use the tools to find out which books the library can lend. Answer in one sentence.")  # <3>
    @abstractmethod
    def chat(self, message: str) -> str:
        ...
