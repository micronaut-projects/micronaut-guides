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

import io.micronaut.context.ApplicationContext
import io.micronaut.runtime.server.EmbeddedServer
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import io.micronaut.test.support.TestPropertyProvider
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

@MicronautTest(startApplication = false) // <1>
@TestInstance(TestInstance.Lifecycle.PER_CLASS) // <2>
class AssistantTest : TestPropertyProvider { // <3>

    private lateinit var mcpServer: EmbeddedServer

    override fun getProperties(): Map<String, String> {
        mcpServer = ApplicationContext.run(EmbeddedServer::class.java, mapOf<String, Any>( // <4>
            "spec.name" to "DiskSpaceMcpServer",
            "micronaut.server.port" to "-1",
            "micronaut.mcp.server.info.name" to "diskspace",
            "micronaut.mcp.server.info.version" to "0.0.1",
            "micronaut.mcp.server.transport" to "HTTP"))
        return mapOf("micronaut.mcp.client.http.diskspace.url" to "${mcpServer.url}/mcp") // <5>
    }

    @AfterAll
    fun stopMcpServer() {
        mcpServer.close()
    }

    @Test
    fun theAssistantCallsTheMcpTool(assistant: Assistant, chatModel: ScriptedChatModel) { // <6>
        val answer = assistant.chat("How much free disk space do I have?")

        assertEquals("The MCP server reports: Free disk space: 42 GB", answer) // <7>

        val tools = chatModel.requests.first().toolSpecifications() // <8>
        assertTrue(tools.any { it.name() == "freeDiskSpace" })
    }
}
