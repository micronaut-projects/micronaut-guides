import json

import pytest
import requests

from pyronaut.test import MicronautTest, micronaut_test_fixture


@pytest.fixture
def my_context(request):
    fixture = micronaut_test_fixture(
        request,
        MicronautTest(transactional=False),  # <1>
    )
    yield fixture
    fixture.stop()


@pytest.fixture
def client(my_context):
    return requests.with_context(my_context)  # <2>


def test_product_schema(client):
    response = client.get("/schemas/product.schema.json")

    assert response.status_code == 200
    assert json.loads(response.text) == {
        "$schema": "https://json-schema.org/draft/2020-12/schema",
        "$id": "http://localhost:8080/schemas/product.schema.json",
        "title": "Product",
        "description": "A product from Acme's catalog",
        "type": "object",
        "properties": {
            "productId": {
                "type": "integer",
            },
            "productName": {
                "type": "string",
                "minLength": 1,
            },
            "price": {
                "type": "number",
                "exclusiveMinimum": 0,
            },
            "tags": {
                "type": "array",
                "items": {
                    "type": "string",
                },
                "minItems": 1,
                "uniqueItems": True,
            },
        },
        "required": ["productId", "productName", "price"],
    }
