from dataclasses import dataclass
from typing import Annotated

from java.math import BigDecimal
from micronaut.data.annotation import GeneratedValue, Id, MappedEntity


@dataclass
@MappedEntity("products")
class Product:
    id: Annotated[int | None, Id, GeneratedValue]
    code: str
    name: str
    price: BigDecimal
