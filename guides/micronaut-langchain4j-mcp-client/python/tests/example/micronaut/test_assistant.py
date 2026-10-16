import pytest

from micronaut.context import ApplicationContext
from micronaut.runtime.server import EmbeddedServer
from pyronaut.test import MicronautTest, micronaut_test_fixture


@pytest.fixture
def mcp_server():
    server = ApplicationContext.run(EmbeddedServer, {  # <1>
        "spec.name": "DiskSpaceMcpServer",
        "micronaut.server.port": "-1",
        "micronaut.mcp.server.info.name": "diskspace",
        "micronaut.mcp.server.info.version": "0.0.1",
        "micronaut.mcp.server.transport": "HTTP",
    })
    yield server
    server.close()


@pytest.fixture
def my_context(request, mcp_server):
    fixture = micronaut_test_fixture(request, MicronautTest(
        start_application=False,
        properties={"micronaut.mcp.client.http.diskspace.url": f"{mcp_server.getURL()}/mcp"},  # <2>
    ))
    yield fixture
    fixture.stop()


def test_the_assistant_calls_the_mcp_tool(my_context):
    assistant = my_context["example.micronaut.Assistant"]  # <3>
    chat_model = my_context["example.micronaut.ScriptedChatModel"]

    answer = assistant.chat("How much free disk space do I have?")

    assert answer == "The MCP server reports: Free disk space: 42 GB"  # <4>

    tools = chat_model.requests[0].toolSpecifications()  # <5>
    assert any(tool.name() == "freeDiskSpace" for tool in tools)
