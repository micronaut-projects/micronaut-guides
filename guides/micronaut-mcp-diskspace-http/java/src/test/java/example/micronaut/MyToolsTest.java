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

import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import io.modelcontextprotocol.client.McpSyncClient;
import io.modelcontextprotocol.spec.McpSchema;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.util.CollectionUtils;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

@MicronautTest
class MyToolsTest {

    @Test
    void mcpCallTool(McpSyncClient mcpClient) {
        McpSchema.CallToolResult callToolResult = assertDoesNotThrow(() ->
                mcpClient.callTool(new McpSchema.CallToolRequest("freeDiskSpace", Collections.emptyMap())));
        assertEquals(1, callToolResult.content().size());
        assertInstanceOf(McpSchema.TextContent.class, callToolResult.content().get(0));
        McpSchema.TextContent textContent = (McpSchema.TextContent) callToolResult.content().get(0);
        String response = textContent.text();
        assertTrue(response.contains("Free disk space"));
    }

    @Test
    void mcpListTools(McpSyncClient mcpClient) {
        McpSchema.ListToolsResult listToolsResult = assertDoesNotThrow(() ->
                mcpClient.listTools());

        assertEquals(1, listToolsResult.tools().size());
        McpSchema.Tool tool = listToolsResult.tools().get(0);
        assertEquals("freeDiskSpace", tool.name());
        assertEquals("Free Disk Space", tool.title());
        assertEquals("Return the free disk space in the users computer", tool.description());
    }
}