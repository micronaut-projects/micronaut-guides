import uuid

import pytest

from pyronaut.test import MicronautTest, micronaut_test_fixture

from example.micronaut.domain.thing import Thing


@pytest.fixture
def my_context(request):
    fixture = micronaut_test_fixture(
        request,
        MicronautTest(
            environments=["test"],
            properties={
                "oci.config.enabled": "false",
            },
            start_application=False,
            transactional=False,
        ),
    )
    yield fixture
    fixture.stop()


@pytest.fixture
def thing_repository(my_context):
    return my_context["example.micronaut.repository.ThingRepository"]


@pytest.fixture(autouse=True)
def clean_database(thing_repository):
    thing_repository.deleteAll()
    yield
    thing_repository.deleteAll()


def test_find_all(thing_repository):
    assert thing_repository.count() == 0

    thing_repository.save(Thing("t1"))
    thing_repository.save(Thing("t2"))
    thing_repository.save(Thing("t3"))

    things = thing_repository.findAll()
    assert len(things) == 3
    assert sorted(thing.name for thing in things) == ["t1", "t2", "t3"]


def test_find_by_name(thing_repository):
    name = str(uuid.uuid4())

    assert thing_repository.findByName(name).isEmpty()

    thing_repository.save(Thing(name))
    thing = thing_repository.findByName(name).orElse(None)
    assert thing is not None
    assert thing.name == name
