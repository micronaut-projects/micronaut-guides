import pytest

from example.micronaut.product import Product
from pyronaut.test import MicronautTest, Sql, micronaut_test_fixture


@pytest.fixture
def my_context(request):
    fixture = micronaut_test_fixture(
        request,
        MicronautTest(
            start_application=False,
            transactional=True,  # <1>
            sql=Sql(
                scripts="classpath:sql/seed-data.sql",
                phase=Sql.Phase.BEFORE_EACH,
            ),  # <2>
        ),
    )
    yield fixture
    fixture.stop()


@pytest.fixture
def product_repository(my_context):
    return my_context["example.micronaut.ProductRepository"]


def test_should_get_all_products(product_repository):
    products = list(product_repository.findAll())

    assert len(products) == 2


def test_should_not_create_a_product_with_duplicate_code(product_repository):
    product = Product(3, "p101", "Test Product")
    product_repository.createProductIfNotExists(product)
    optional_product = product_repository.findById(product.id)

    assert optional_product.isEmpty()
