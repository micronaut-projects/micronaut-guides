package example.micronaut.resources;

import example.micronaut.domain.NameDto;
import example.micronaut.domain.Pet;
import example.micronaut.repositories.PetRepository;
import io.micronaut.data.model.Pageable;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import java.util.List;
import java.util.Optional;

@Path("/pets")
class PetResource {

    private final PetRepository petRepository;

    PetResource(PetRepository petRepository) {
        this.petRepository = petRepository;
    }

    @GET
    List<NameDto> all(Pageable pageable) {
        return petRepository.list(pageable);
    }

    @GET
    @Path("/{name}")
    Optional<Pet> byName(@PathParam("name") String petsName) {
        return petRepository.findByName(petsName);
    }

}