import java
import pytest

from pyronaut.test import MicronautTest, micronaut_test_fixture


CallToolRequest = java.type("io.modelcontextprotocol.spec.McpSchema$CallToolRequest")
Collections = java.type("java.util.Collections")
McpSyncClient = java.type("io.modelcontextprotocol.client.McpSyncClient")
TextContent = java.type("io.modelcontextprotocol.spec.McpSchema$TextContent")


@pytest.fixture
def my_context(request):
    fixture = micronaut_test_fixture(
        request,
        MicronautTest(environments=["test"], transactional=False),
    )
    yield fixture
    fixture.stop()


@pytest.fixture
def mcp_client(my_context):
    return my_context[McpSyncClient]


def test_mcp_call_tool(mcp_client):
    call_tool_result = mcp_client.callTool(
        CallToolRequest("freeDiskSpace", Collections.emptyMap())
    )

    content = call_tool_result.content()
    assert content.size() == 1

    text_content = content.get(0)
    assert isinstance(text_content, TextContent)
    assert "Free disk space" in text_content.text()


def test_mcp_list_tools(mcp_client):
    list_tools_result = mcp_client.listTools()

    tools = list_tools_result.tools()
    assert tools.size() == 1

    tool = tools.get(0)
    assert tool.name() == "freeDiskSpace"
    assert tool.title() == "Free Disk Space"
    assert tool.description() == "Return the free disk space in the users computer"
