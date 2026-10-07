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

import dev.langchain4j.service.MemoryId
import dev.langchain4j.service.SystemMessage
import dev.langchain4j.service.UserMessage
import io.micronaut.langchain4j.annotation.AiService

@AiService // <1>
interface Assistant {

    @SystemMessage("You are a friendly assistant that helps developers learn the Micronaut framework. Answer in no more than three sentences.") // <2>
    String chat(@MemoryId String conversationId, // <3>
                @UserMessage String message) // <4>
}
