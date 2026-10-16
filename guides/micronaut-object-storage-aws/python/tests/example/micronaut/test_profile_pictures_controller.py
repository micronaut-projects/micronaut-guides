import java
import pytest
import requests
from pyronaut.test import MicronautTest, micronaut_test_fixture
from software.amazon.awssdk.services.s3 import S3Client
from software.amazon.awssdk.services.s3.model import (
    CreateBucketRequest,
    DeleteBucketRequest,
    DeleteObjectRequest,
    GetObjectRequest,
)


BUCKET_NAME = "micronaut-guide-object-storage"

DockerImageName = java.type("org.testcontainers.utility.DockerImageName")
FlociContainer = java.type("io.floci.testcontainers.FlociContainer")
FLOCI_IMAGE = DockerImageName.parse("floci/floci:1.5.18")


@pytest.fixture(scope="module")
def floci_container():
    floci = FlociContainer(FLOCI_IMAGE)
    floci.start()
    try:
        yield floci
    finally:
        floci.stop()


@pytest.fixture
def my_context(request, floci_container):
    fixture = micronaut_test_fixture(
        request,
        MicronautTest(
            properties={
                "aws.access-key-id": str(floci_container.getAccessKey()),
                "aws.secret-key": str(floci_container.getSecretKey()),
                "aws.region": str(floci_container.getRegion()),
                "aws.services.s3.endpoint-override": str(floci_container.getEndpoint()),
                "aws.services.s3.path-style-access-enabled": "true",
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
def s3(my_context) -> S3Client:
    return my_context[S3Client]


@pytest.fixture(autouse=True)
def bucket(s3):
    s3.createBucket(CreateBucketRequest.builder().bucket(BUCKET_NAME).build())
    try:
        yield
    finally:
        try:
            s3.deleteObject(
                DeleteObjectRequest.builder()
                .bucket(BUCKET_NAME)
                .key("alvaro.jpg")
                .build()
            )
        except Exception:
            pass
        s3.deleteBucket(DeleteBucketRequest.builder().bucket(BUCKET_NAME).build())


def test_it_works(client, s3):
    response = client.post(
        "/pictures/alvaro",
        files={"fileUpload": ("test-file.txt", b"micronaut", "text/plain")},
    )

    assert response.status_code == 201
    assert response.headers["location"]
    assert response.headers["ETag"]

    stored = s3.getObject(
        GetObjectRequest.builder().key("alvaro.jpg").bucket(BUCKET_NAME).build()
    )
    assert bytes(stored.readAllBytes()).decode() == "micronaut"

    download = client.get(response.headers["location"])

    assert download.status_code == 200
    assert download.content == b"micronaut"

    response = client.delete(response.headers["location"])

    assert response.status_code == 204
