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
import io.micronaut.context.ApplicationContext
import io.micronaut.runtime.server.EmbeddedServer
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import io.micronaut.test.support.TestPropertyProvider
import jakarta.inject.Inject
import spock.lang.AutoCleanup
import spock.lang.Shared
import spock.lang.Specification

@MicronautTest(startApplication = false) // <1>
class AssistantSpec extends Specification implements TestPropertyProvider { // <2>

    @Shared
    @AutoCleanup // <3>
    EmbeddedServer mcpServer = ApplicationContext.run(EmbeddedServer, [ // <4>
            "spec.name"                        : "DiskSpaceMcpServer",
            "micronaut.server.port"            : "-1",
            "micronaut.mcp.server.info.name"   : "diskspace",
            "micronaut.mcp.server.info.version": "0.0.1",
            "micronaut.mcp.server.transport"   : "HTTP"])

    @Override
    Map<String, String> getProperties() {
        ["micronaut.mcp.client.http.diskspace.url": "${mcpServer.URL}/mcp".toString()] // <5>
    }

    @Inject
    Assistant assistant // <6>

    @Inject
    ScriptedChatModel chatModel

    void "the assistant calls the MCP tool"() {
        when:
        String answer = assistant.chat("How much free disk space do I have?")

        then:
        answer == "The MCP server reports: Free disk space: 42 GB" // <7>

        when:
        List<ToolSpecification> tools = chatModel.requests.first().toolSpecifications() // <8>

        then:
        tools.any { it.name() == "freeDiskSpace" }
    }
}
