import pytest

from pyronaut.test import MicronautTest, Sql, micronaut_test_fixture


@pytest.fixture
def my_context(request):
    fixture = micronaut_test_fixture(
        request,
        MicronautTest(
            start_application=False,
            transactional=True,
            sql=Sql(
                scripts=["classpath:sql/init-db.sql", "classpath:sql/seed-data.sql"],
                phase=Sql.Phase.BEFORE_EACH,
            ),  # <1>
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
