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
import io.micronaut.context.ApplicationContext;
import io.micronaut.runtime.server.EmbeddedServer;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import io.micronaut.test.support.TestPropertyProvider;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@MicronautTest(startApplication = false) // <1>
@TestInstance(TestInstance.Lifecycle.PER_CLASS) // <2>
class AssistantTest implements TestPropertyProvider { // <3>

    private EmbeddedServer mcpServer;

    @Override
    public Map<String, String> getProperties() {
        mcpServer = ApplicationContext.run(EmbeddedServer.class, Map.of( // <4>
                "spec.name", "DiskSpaceMcpServer",
                "micronaut.server.port", "-1",
                "micronaut.mcp.server.info.name", "diskspace",
                "micronaut.mcp.server.info.version", "0.0.1",
                "micronaut.mcp.server.transport", "HTTP"));
        return Map.of("micronaut.mcp.client.http.diskspace.url", mcpServer.getURL() + "/mcp"); // <5>
    }

    @AfterAll
    void stopMcpServer() {
        mcpServer.close();
    }

    @Test
    void theAssistantCallsTheMcpTool(Assistant assistant, ScriptedChatModel chatModel) { // <6>
        String answer = assistant.chat("How much free disk space do I have?");

        assertEquals("The MCP server reports: Free disk space: 42 GB", answer); // <7>

        List<ToolSpecification> tools = chatModel.getRequests().getFirst().toolSpecifications(); // <8>
        assertTrue(tools.stream().anyMatch(tool -> tool.name().equals("freeDiskSpace")));
    }
}
