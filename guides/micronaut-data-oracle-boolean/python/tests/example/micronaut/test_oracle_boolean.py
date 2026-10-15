import pytest
import requests
from micronaut.transaction import TransactionOperations
from pyronaut.test import MicronautTest, micronaut_test_fixture

from example.micronaut.task import Task
from example.micronaut.task_repository import TaskRepository


@pytest.fixture
def my_context(request):
    fixture = micronaut_test_fixture(
        request,
        MicronautTest(transactional=False),  # <1>
    )
    yield fixture
    fixture.stop()


@pytest.fixture
def repository(my_context):
    repository = my_context[TaskRepository]
    repository.deleteAll()
    return repository


@pytest.fixture
def client(my_context):
    return requests.with_context(my_context)


def test_completed_column_is_native_boolean(my_context):
    assert column_data_type(my_context[TransactionOperations], "TASK", "COMPLETED") == "BOOLEAN"  # <2>


def test_queries_boolean_values(repository):
    write = repository.save(Task("Write the guide"))
    review = repository.save(Task("Review the guide"))

    assert repository.updateCompleted(write.id, True) == 1  # <3>

    assert titles(repository.findByCompletedTrue()) == ["Write the guide"]  # <4>
    assert titles(repository.findByCompletedFalse()) == ["Review the guide"]
    assert titles(repository.findByCompleted(True)) == ["Write the guide"]  # <5>

    assert repository.findById(write.id).orElseThrow().completed
    assert not repository.findById(review.id).orElseThrow().completed


def test_completes_task_over_http(repository, client):
    task = client.post("/tasks", json={"title": "Publish the guide"}).json()
    assert task["completed"] is False

    completed = client.put(f"/tasks/{task['id']}/complete").json()  # <6>
    assert completed["completed"] is True

    assert [t["title"] for t in client.get("/tasks/completed").json()] == ["Publish the guide"]
    assert client.get("/tasks/open").json() == []


def titles(tasks) -> list[str]:
    return [task.title for task in tasks]


def column_data_type(transaction_operations: TransactionOperations, table: str, column: str) -> str | None:
    def read(status):
        sql = "SELECT data_type FROM user_tab_columns WHERE table_name = ? AND column_name = ?"
        statement = status.getConnection().prepareStatement(sql)
        try:
            statement.setString(1, table)
            statement.setString(2, column)
            result = statement.executeQuery()
            return result.getString(1) if result.next() else None
        finally:
            statement.close()

    return transaction_operations.executeRead(read)
