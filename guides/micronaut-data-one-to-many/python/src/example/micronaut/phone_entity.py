from __future__ import annotations

from dataclasses import dataclass
from typing import TYPE_CHECKING, Annotated

from micronaut.data.annotation import GeneratedValue, Id, MappedEntity, Relation

if TYPE_CHECKING:
    from .contact_entity import ContactEntity


@dataclass
@MappedEntity("phone")  # <1>
class PhoneEntity:
    id: Annotated[int | None, Id, GeneratedValue]  # <2> <3> <4>
    phone: str
    contact: Annotated[ContactEntity, Relation(value=Relation.Kind.MANY_TO_ONE)]  # <5>
