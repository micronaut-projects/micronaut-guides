import pytest
from org.testcontainers.containers import PostgreSQLContainer


@pytest.fixture(scope="session")
def datasource():
    container = PostgreSQLContainer("postgres:15.3-alpine")
    container.start()  # <1>
    try:
        yield {  # <2>
            "datasources.default.driver-class-name": "org.postgresql.Driver",
            "datasources.default.url": str(container.getJdbcUrl()),
            "datasources.default.username": str(container.getUsername()),
            "datasources.default.password": str(container.getPassword()),
        }
    finally:
        container.stop()
