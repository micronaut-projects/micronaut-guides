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

import dev.langchain4j.agent.tool.ToolSpecification
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import jakarta.inject.Inject
import spock.lang.Specification

@MicronautTest(startApplication = false) // <1>
class LibraryAssistantSpec extends Specification {

    @Inject
    LibraryAssistant assistant // <2>

    @Inject
    ScriptedChatModel chatModel

    void "the assistant calls the tool"() {
        when:
        String answer = assistant.chat("How many copies of Dune can I borrow?")

        then:
        answer == "The library can lend 3 copies of Dune." // <3>

        when:
        ToolSpecification tool = chatModel.requests.first().toolSpecifications().first() // <4>

        then:
        tool.name() == "availableCopies"
        tool.description() == "Returns the number of copies of a book that the library can lend right now"
        tool.parameters().properties().containsKey("title")
    }
}
