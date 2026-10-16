from dev.langchain4j.agent.tool import ToolExecutionRequest
from dev.langchain4j.data.message import AiMessage, ToolExecutionResultMessage
from dev.langchain4j.model.chat import ChatModel
from dev.langchain4j.model.chat.request import ChatRequest
from dev.langchain4j.model.chat.response import ChatResponse
from jakarta.inject import Singleton
from micronaut.context.annotation import Replaces


@Singleton
@Replaces(ChatModel)  # <1>
class ScriptedChatModel(ChatModel):

    def __init__(self):
        self.requests = []

    def doChat(self, request: ChatRequest) -> ChatResponse:
        self.requests.append(request)
        last_message = request.messages().getLast()
        if isinstance(last_message, ToolExecutionResultMessage):  # <2>
            return self._response(AiMessage.from_(f"The MCP server reports: {last_message.text()}"))
        tool_execution_request = (ToolExecutionRequest.builder()  # <3>
                                  .id("1")
                                  .name("freeDiskSpace")
                                  .arguments("{}")
                                  .build())
        return self._response(AiMessage.from_(tool_execution_request))

    @staticmethod
    def _response(ai_message: AiMessage) -> ChatResponse:
        return ChatResponse.builder().aiMessage(ai_message).build()
