from contextlib import contextmanager

import java
import pytest
from micronaut.security.authentication import Authentication
from org.testcontainers.oracle import OracleContainer
from org.testcontainers.utility import DockerImageName, MountableFile
from pyronaut.test import MicronautTest, Sql, micronaut_test_fixture

IMAGE_NAME = "gvenzl/oracle-free:23.26.1-faststart"  # <1>
# The faststart image already has a database, so startup scripts are the
# reliable hook for schema creation.
INIT_SCRIPT = "/container-entrypoint-startdb.d/01-hr-schema.sql"
EMPLOYEES_TEST_DATA = "classpath:employees-test-data.sql"

# Internal to Micronaut Security, so it has no generated stub; loaded by its Java name.
ServerRequestContextSecurityContextSupplier = java.type(
    "io.micronaut.security.context.ServerRequestContextSecurityContextSupplier"
)


@pytest.fixture(scope="session")
def oracle():
    container = OracleContainer(DockerImageName.parse(IMAGE_NAME)).withCopyFileToContainer(
        MountableFile.forClasspathResource("oracle-init.sql"), INIT_SCRIPT
    )  # <2>
    container.start()  # <3>
    try:
        yield {  # <4>
            "datasources.default.url": str(container.getJdbcUrl()),
            "datasources.default.username": str(container.getUsername()),
            "datasources.default.password": str(container.getPassword()),
        }
    finally:
        container.stop()


@pytest.fixture
def application_context(request, oracle):
    fixture = micronaut_test_fixture(
        request,
        MicronautTest(
            environments=["test"],
            start_application=False,  # <5>
            properties=oracle,
            sql=Sql(EMPLOYEES_TEST_DATA),  # <6>
        ),
    )
    yield fixture
    fixture.stop()


@pytest.fixture
def run_as():
    @contextmanager
    def _run_as(username: str):  # <7>
        propagated = ServerRequestContextSecurityContextSupplier.withAuthentication(Authentication.build(username))
        scope = propagated.propagate()
        try:
            yield
        finally:
            scope.close()

    return _run_as
