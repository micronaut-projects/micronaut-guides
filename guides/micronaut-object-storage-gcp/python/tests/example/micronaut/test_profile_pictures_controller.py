from typing import Annotated
from urllib.request import Request, urlopen

import pytest
import requests
from com.google.cloud import NoCredentials
from com.google.cloud.storage import BlobId, BucketInfo, Storage, StorageOptions
from jakarta.inject import Singleton
from micronaut.context.annotation import Factory, Primary, Requires, Value
from pyronaut.test import MicronautTest, micronaut_test_fixture
from org.testcontainers.containers import GenericContainer
from org.testcontainers.utility import DockerImageName


BUCKET_NAME = "micronaut-guide-object-storage"
SPEC_NAME = "ProfilePicturesControllerTest"

FAKE_GCS_IMAGE = DockerImageName.parse("fsouza/fake-gcs-server:1.40.1")
FAKE_GCS_PORT = 4443
fake_gcs_url = None


@pytest.fixture(scope="module")
def fake_gcs_server():
    container = GenericContainer(FAKE_GCS_IMAGE)
    container.withExposedPorts(FAKE_GCS_PORT)
    container.withCreateContainerCmdModifier(
        lambda cmd: cmd.withEntrypoint("/bin/fake-gcs-server", "-scheme", "http")
    )
    container.start()
    global fake_gcs_url
    fake_gcs_url = f"http://{container.getHost()}:{container.getMappedPort(FAKE_GCS_PORT)}"
    configure_external_url(fake_gcs_url)
    try:
        yield
    finally:
        fake_gcs_url = None
        container.stop()


def configure_external_url(url: str) -> None:
    body = f'{{"externalUrl":"{url}"}}'.encode()
    request = Request(
        f"{url}/_internal/config",
        data=body,
        headers={"Content-Type": "application/json"},
        method="PUT",
    )
    with urlopen(request, timeout=10):
        pass


@Factory
@Requires(property="spec.name", value="ProfilePicturesControllerTest")
class FakeGcsFactory:
    @Singleton
    @Primary
    def storage(self, url: Annotated[str, Value("${fake.gcs.url}")]) -> Storage:
        return (
            StorageOptions.newBuilder()
            .setHost(url)
            .setProjectId("test-project")
            .setCredentials(NoCredentials.getInstance())
            .build()
            .getService()
        )


@pytest.fixture
def my_context(request, fake_gcs_server):
    fixture = micronaut_test_fixture(
        request,
        MicronautTest(
            properties={
                "spec.name": SPEC_NAME,
                "fake.gcs.url": fake_gcs_url,
            },
            transactional=False,
        ),
    )
    yield fixture
    fixture.stop()


@pytest.fixture
def client(my_context):
    return requests.with_context(my_context)


@pytest.fixture
def storage(my_context) -> Storage:
    return my_context[Storage]


@pytest.fixture(autouse=True)
def bucket(storage):
    storage.create(BucketInfo.newBuilder(BUCKET_NAME).build())
    try:
        yield
    finally:
        blob = storage.get(BlobId.of(BUCKET_NAME, "alvaro.jpg"))
        if blob is not None:
            blob.delete()
        bucket = storage.get(BUCKET_NAME)
        if bucket is not None:
            bucket.delete()


def test_it_works(client, storage):
    response = client.post(
        "/pictures/alvaro",
        files={"fileUpload": ("test-file.txt", b"micronaut", "text/plain")},
    )

    assert response.status_code == 201
    assert response.headers["location"]
    assert response.headers["ETag"]

    blob = storage.get(BlobId.of(BUCKET_NAME, "alvaro.jpg"))
    assert bytes(blob.getContent()).decode() == "micronaut"

    download = client.get(response.headers["location"])

    assert download.status_code == 200
    assert download.content == b"micronaut"

    response = client.delete(response.headers["location"])

    assert response.status_code == 204
