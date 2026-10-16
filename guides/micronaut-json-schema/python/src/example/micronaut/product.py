from dataclasses import dataclass
from typing import Annotated

from jakarta.validation.constraints import NotBlank, NotNull, Positive, Size
from micronaut.jsonschema import JsonSchema
from micronaut.serde.annotation import Serdeable


@Serdeable  # <1>
@dataclass
@JsonSchema(description="A product from Acme's catalog")  # <2>
class Product:
    productId: Annotated[int, NotNull]  # <3>
    productName: Annotated[str, NotNull, NotBlank]
    price: Annotated[float, NotNull, Positive]  # <3> <4>
    tags: Annotated[set[str] | None, Size(min=1)] = None  # <4>
