package example.micronaut.resources;

import example.micronaut.domain.NameDTO;
import example.micronaut.domain.Pet;
import example.micronaut.repositories.PetRepository;
import io.micronaut.data.model.Pageable;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import java.util.List;
import java.util.Optional;

@Path("/pets")
class PetResource {

    private final PetRepository petRepository;

    PetResource(PetRepository petRepository) {
        this.petRepository = petRepository;
    }

    @GET
    List<NameDTO> all(Pageable pageable) {
        return petRepository.list(pageable);
    }

    @GET
    @Path("/{name}")
    Optional<Pet> byName(String name) {
        return petRepository.findByName(name);
    }

}