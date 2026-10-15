from __future__ import annotations

from dataclasses import dataclass, field
from typing import TYPE_CHECKING, Annotated

from micronaut.data.annotation import GeneratedValue, Id, MappedEntity, Relation

if TYPE_CHECKING:
    from .phone_entity import PhoneEntity


@dataclass
@MappedEntity("contact")  # <1>
class ContactEntity:
    id: Annotated[int | None, Id, GeneratedValue]  # <2> <3> <4>
    firstName: str
    lastName: str
    phones: Annotated[
        list[PhoneEntity],
        Relation(value=Relation.Kind.ONE_TO_MANY, mappedBy="contact"),
    ] = field(default_factory=list)  # <5>
