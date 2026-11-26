from dev.langchain4j.mcp import McpToolProvider
from jakarta.inject import Singleton
from micronaut.langchain4j.aiservices import AiServiceCreationContext, AiServiceCustomizer

from .assistant import Assistant


@Singleton  # <1>
class AssistantCustomizer(AiServiceCustomizer[Assistant]):  # <2>

    def __init__(self, mcp_tool_provider: McpToolProvider):  # <3>
        self.mcp_tool_provider = mcp_tool_provider

    def customize(self, creation_context: AiServiceCreationContext[Assistant]) -> None:
        creation_context.aiServices().toolProvider(self.mcp_tool_provider)  # <4>
