/*
 * Copyright 2017-2026 original authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package example.micronaut

import dev.langchain4j.agent.tool.ToolExecutionRequest
import dev.langchain4j.data.message.AiMessage
import dev.langchain4j.data.message.ToolExecutionResultMessage
import dev.langchain4j.model.chat.ChatModel
import dev.langchain4j.model.chat.request.ChatRequest
import dev.langchain4j.model.chat.response.ChatResponse
import io.micronaut.context.annotation.Replaces
import jakarta.inject.Singleton
import java.util.concurrent.CopyOnWriteArrayList

@Replaces(ChatModel::class) // <1>
@Singleton
class ScriptedChatModel : ChatModel {

    val requests = CopyOnWriteArrayList<ChatRequest>()

    override fun doChat(request: ChatRequest): ChatResponse {
        requests.add(request)
        val lastMessage = request.messages().last()
        if (lastMessage is ToolExecutionResultMessage) { // <2>
            return response(AiMessage.from("The library can lend ${lastMessage.text()} copies of Dune."))
        }
        val toolExecutionRequest = ToolExecutionRequest.builder() // <3>
            .id("1")
            .name("availableCopies")
            .arguments("""{"title": "Dune"}""")
            .build()
        return response(AiMessage.from(toolExecutionRequest))
    }

    private fun response(aiMessage: AiMessage): ChatResponse =
        ChatResponse.builder().aiMessage(aiMessage).build()
}
