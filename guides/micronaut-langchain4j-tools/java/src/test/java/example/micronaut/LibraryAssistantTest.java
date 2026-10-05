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
package example.micronaut;

import dev.langchain4j.agent.tool.ToolSpecification;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@MicronautTest(startApplication = false) // <1>
class LibraryAssistantTest {

    @Test
    void theAssistantCallsTheTool(LibraryAssistant assistant, ScriptedChatModel chatModel) { // <2>
        String answer = assistant.chat("How many copies of Dune can I borrow?");

        assertEquals("The library can lend 3 copies of Dune.", answer); // <3>

        ToolSpecification tool = chatModel.getRequests().getFirst().toolSpecifications().getFirst(); // <4>
        assertEquals("availableCopies", tool.name());
        assertEquals("Returns the number of copies of a book that the library can lend right now", tool.description());
        assertTrue(tool.parameters().properties().containsKey("title"));
    }
}
