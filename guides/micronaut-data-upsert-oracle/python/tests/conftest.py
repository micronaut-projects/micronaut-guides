import pytest
from org.testcontainers.oracle import OracleContainer
from org.testcontainers.utility import DockerImageName

IMAGE_NAME = "gvenzl/oracle-free:23.26.1-faststart"


@pytest.fixture(scope="session")
def datasource():
    container = OracleContainer(DockerImageName.parse(IMAGE_NAME))
    container.start()  # <1>
    try:
        yield {  # <2>
            "datasources.default.driver-class-name": "oracle.jdbc.OracleDriver",
            "datasources.default.url": str(container.getJdbcUrl()),
            "datasources.default.username": str(container.getUsername()),
            "datasources.default.password": str(container.getPassword()),
        }
    finally:
        container.stop()
