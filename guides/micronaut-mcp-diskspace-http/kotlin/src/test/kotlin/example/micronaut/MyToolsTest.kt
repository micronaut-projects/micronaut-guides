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

import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import io.modelcontextprotocol.client.McpSyncClient
import io.modelcontextprotocol.spec.McpSchema
import org.junit.jupiter.api.Assertions.assertDoesNotThrow
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertInstanceOf
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

@MicronautTest
class MyToolsTest {

    @Test
    fun mcpCallTool(mcpClient: McpSyncClient) {
        val callToolResult = assertDoesNotThrow<McpSchema.CallToolResult> {
            mcpClient.callTool(McpSchema.CallToolRequest("freeDiskSpace", emptyMap()))
        }
        assertEquals(1, callToolResult.content().size)
        assertInstanceOf(McpSchema.TextContent::class.java, callToolResult.content()[0])
        val textContent = callToolResult.content()[0] as McpSchema.TextContent
        val response = textContent.text()
        assertTrue(response.contains("Free disk space"))
    }

    @Test
    fun mcpListTools(mcpClient: McpSyncClient) {
        val listToolsResult = assertDoesNotThrow<McpSchema.ListToolsResult> {
            mcpClient.listTools()
        }

        assertEquals(1, listToolsResult.tools().size)
        val tool = listToolsResult.tools()[0]
        assertEquals("freeDiskSpace", tool.name())
        assertEquals("Free Disk Space", tool.title())
        assertEquals("Return the free disk space in the user's computer", tool.description())
    }
}
