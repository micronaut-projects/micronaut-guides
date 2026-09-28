from typing import Annotated

from jakarta.validation import Valid
from jakarta.validation.constraints import NotNull
from jakarta.ws.rs import GET, POST, Path, PathParam
from micronaut.http import HttpStatus
from micronaut.http.annotation import Body, Status

from .name_dto import NameDto
from .pet import Pet
from .pet_repository import PetRepository
from .pet_save import PetSave


@Path("/pets")  # <1>
class PetResource:
    def __init__(self, pet_repository: PetRepository):  # <2>
        self.pet_repository = pet_repository

    @GET  # <3>
    def all(self) -> list[NameDto]:  # <4>
        return self.pet_repository.list()

    @GET  # <3>
    @Path("/{name}")  # <5>
    def by_name(self, pets_name: Annotated[str, PathParam("name")]) -> Pet | None:  # <6>
        return self.pet_repository.findByName(pets_name).orElse(None)

    @POST
    @Status(HttpStatus.CREATED)
    def save(self, pet_save: Annotated[PetSave, Body, NotNull, Valid]) -> None:
        self.pet_repository.save(pet_save.name, pet_save.type)
