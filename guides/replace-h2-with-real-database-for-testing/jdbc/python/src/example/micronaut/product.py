from dataclasses import dataclass
from typing import Annotated

from micronaut.data.annotation import Id, MappedEntity


@dataclass
@MappedEntity("products")  # <1>
class Product:
    id: Annotated[int, Id]  # <2>
    code: str
    name: str
