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

import io.micronaut.test.extensions.spock.annotation.MicronautTest
import io.modelcontextprotocol.client.McpSyncClient
import io.modelcontextprotocol.spec.McpSchema
import jakarta.inject.Inject
import spock.lang.Specification

@MicronautTest
class MyToolsSpec extends Specification {

    @Inject
    McpSyncClient mcpClient

    void mcpCallTool() {
        when:
        McpSchema.CallToolResult callToolResult =
                mcpClient.callTool(new McpSchema.CallToolRequest('freeDiskSpace', Collections.emptyMap()))

        then:
        callToolResult.content().size() == 1
        callToolResult.content().first() instanceof McpSchema.TextContent

        when:
        String response = ((McpSchema.TextContent) callToolResult.content().first()).text()

        then:
        response.contains('Free disk space')
    }

    void mcpListTools() {
        when:
        McpSchema.ListToolsResult listToolsResult = mcpClient.listTools()

        then:
        listToolsResult.tools().size() == 1

        when:
        McpSchema.Tool tool = listToolsResult.tools().first()

        then:
        tool.name() == 'freeDiskSpace'
        tool.title() == 'Free Disk Space'
        tool.description() == 'Return the free disk space in the users computer'
    }
}
