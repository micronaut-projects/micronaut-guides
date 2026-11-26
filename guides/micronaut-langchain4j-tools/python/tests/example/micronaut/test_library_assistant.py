import pytest

from pyronaut.test import MicronautTest, micronaut_test_fixture


@pytest.fixture
def my_context(request):
    fixture = micronaut_test_fixture(request, MicronautTest(start_application=False))  # <1>
    yield fixture
    fixture.stop()


def test_the_assistant_calls_the_tool(my_context):
    assistant = my_context["example.micronaut.LibraryAssistant"]  # <2>
    chat_model = my_context["example.micronaut.ScriptedChatModel"]

    answer = assistant.chat("How many copies of Dune can I borrow?")

    assert answer == "The library can lend 3 copies of Dune."  # <3>

    tool = chat_model.requests[0].toolSpecifications()[0]  # <4>
    assert tool.name() == "availableCopies"
    assert tool.description() == "Returns the number of copies of a book that the library can lend right now"
    assert tool.parameters().properties().containsKey("title")
