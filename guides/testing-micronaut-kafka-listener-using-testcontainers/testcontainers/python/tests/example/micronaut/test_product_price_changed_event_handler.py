from time import monotonic, sleep

import pytest
from java.math import BigDecimal
from org.testcontainers.kafka import KafkaContainer
from org.testcontainers.utility import DockerImageName
from pyronaut.test import MicronautTest, micronaut_test_fixture

from example.micronaut.product import Product
from example.micronaut.product_price_changed_event import ProductPriceChangedEvent
from example.micronaut.product_repository import ProductRepository
from example.micronaut.test_product_price_changes_client import ProductPriceChangesClient


KAFKA_IMAGE = DockerImageName.parse("apache/kafka:latest")


@pytest.fixture(scope="module")
def kafka_container():
    kafka = KafkaContainer(KAFKA_IMAGE)
    kafka.start()  # <4>
    try:
        yield kafka
    finally:
        kafka.stop()


@pytest.fixture
def my_context(request, kafka_container):
    fixture = micronaut_test_fixture(
        request,
        MicronautTest(
            properties={
                "datasources.default.driver-class-name": "org.testcontainers.jdbc.ContainerDatabaseDriver",  # <2>
                "datasources.default.url": "jdbc:tc:mysql:8:///db",  # <3>
                "kafka.bootstrap.servers": str(kafka_container.getBootstrapServers()),
            },
            transactional=False,
        ),  # <1>
    )
    yield fixture
    fixture.stop()


@pytest.fixture
def product_price_changes_client(my_context) -> ProductPriceChangesClient:
    return my_context["example.micronaut.ProductPriceChangesClient"]


@pytest.fixture
def product_repository(my_context) -> ProductRepository:
    return my_context["example.micronaut.ProductRepository"]


def wait_for_price(product_repository: ProductRepository, product_code: str, price: BigDecimal):
    deadline = monotonic() + 10.0
    while monotonic() < deadline:
        optional_product = product_repository.findByCode(product_code)
        if optional_product.isPresent() and optional_product.get().price.compareTo(price) == 0:
            return optional_product.get()
        sleep(0.1)
    raise AssertionError(f"Timed out waiting for product {product_code} price {price}")


def test_should_handle_product_price_changed_event(
    product_price_changes_client: ProductPriceChangesClient,
    product_repository: ProductRepository,
):
    product = product_repository.save(Product(None, "P100", "Product One", BigDecimal.TEN))  # <5>

    event = ProductPriceChangedEvent("P100", BigDecimal("14.50"))
    product_price_changes_client.send(event.productCode, event)  # <6>

    updated_product = wait_for_price(product_repository, "P100", BigDecimal("14.50"))  # <7>
    assert updated_product.code == "P100"
    assert updated_product.price.compareTo(BigDecimal("14.50")) == 0

    product_repository.deleteById(product.id)
