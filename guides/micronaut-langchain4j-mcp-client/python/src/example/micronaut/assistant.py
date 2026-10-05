from abc import ABC, abstractmethod

from dev.langchain4j.service import SystemMessage
from micronaut.core.annotation import AllowsReflection
from micronaut.langchain4j.annotation import AiService


@AiService  # <1>
@AllowsReflection  # <2>
class Assistant(ABC):

    @SystemMessage("You are an assistant for system administrators. Use the tools to answer questions about the computer. Answer in one sentence.")  # <3>
    @abstractmethod
    def chat(self, message: str) -> str:
        ...
