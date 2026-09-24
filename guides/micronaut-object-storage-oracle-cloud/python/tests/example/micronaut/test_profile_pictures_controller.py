import java
import pytest
import requests
from com.oracle.bmc import Region
from com.oracle.bmc.objectstorage import ObjectStorage
from com.oracle.bmc.objectstorage.model import CreateBucketDetails
from com.oracle.bmc.objectstorage.requests import (
    CreateBucketRequest,
    DeleteBucketRequest,
    GetObjectRequest,
)
from pyronaut.test import MicronautTest, micronaut_test_fixture


BUCKET_NAME = "micronaut-guide-object-storage"
NAMESPACE = "testtenancy"
COMPARTMENT_ID = "ocid1.compartment.oc1..testcompartment"
SPEC_NAME = "ProfilePicturesControllerTest"
OCI_CONTAINER = None

DockerImageName = java.type("org.testcontainers.utility.DockerImageName")
GenericContainer = java.type("org.testcontainers.containers.GenericContainer")
OCI_IMAGE = DockerImageName.parse("cameritelabs/oci-emulator")
OCI_PORT = 12000


def oci_endpoint(container) -> str:
    return f"http://127.0.0.1:{container.getMappedPort(OCI_PORT)}"


@pytest.fixture(scope="module")
def oci_container():
    global OCI_CONTAINER
    container = GenericContainer(OCI_IMAGE).withExposedPorts(OCI_PORT)
    container.start()
    OCI_CONTAINER = container
    try:
        yield container
    finally:
        OCI_CONTAINER = None
        container.stop()


@pytest.fixture
def my_context(request, oci_container):
    fixture = micronaut_test_fixture(
        request,
        MicronautTest(
            properties={
                "spec.name": SPEC_NAME,
                "micronaut.object-storage.oracle-cloud.default.bucket": BUCKET_NAME,
                "micronaut.object-storage.oracle-cloud.default.namespace": NAMESPACE,
                "oci.config.enabled": "false",
                "oci.fingerprint": "50:a6:c1:a1:da:71:57:dc:87:ae:90:af:9c:38:99:67",
                "oci.private-key-file": "file:tests-config/key.pem",
                "oci.region": str(Region.SA_SAOPAULO_1.getRegionId()),
                "oci.tenant-id": "ocid1.tenancy.oc1..testtenancy",
                "oci.user-id": "ocid1.user.oc1..testuser",
            },
            transactional=False,
        ),
    )
    yield fixture
    fixture.stop()


@pytest.fixture
def client(my_context, object_storage):
    return requests.with_context(my_context)


@pytest.fixture
def object_storage(my_context) -> ObjectStorage:
    client = my_context[ObjectStorage]
    client.setEndpoint(oci_endpoint(OCI_CONTAINER))
    return client


@pytest.fixture(autouse=True)
def bucket(object_storage):
    bucket_details = (
        CreateBucketDetails.builder()
        .compartmentId(COMPARTMENT_ID)
        .name(BUCKET_NAME)
        .build()
    )
    object_storage.createBucket(
        CreateBucketRequest.builder()
        .namespaceName(NAMESPACE)
        .createBucketDetails(bucket_details)
        .build()
    )
    try:
        yield
    finally:
        object_storage.deleteBucket(
            DeleteBucketRequest.builder()
            .namespaceName(NAMESPACE)
            .bucketName(BUCKET_NAME)
            .build()
        )

def test_it_works(client, object_storage):
    response = client.post(
        "/pictures/alvaro",
        files={"fileUpload": ("test-file.txt", b"micronaut", "text/plain")},
    )

    assert response.status_code == 201
    assert response.headers["location"]
    assert response.headers["ETag"]

    stored = object_storage.getObject(
        GetObjectRequest.builder()
        .bucketName(BUCKET_NAME)
        .namespaceName(NAMESPACE)
        .objectName("alvaro.jpg")
        .build()
    )
    assert bytes(stored.getInputStream().readAllBytes()).decode() == "micronaut"

    download = client.get(response.headers["location"])

    assert download.status_code == 200
    assert download.content == b"micronaut"

    response = client.delete(response.headers["location"])

    assert response.status_code == 204
