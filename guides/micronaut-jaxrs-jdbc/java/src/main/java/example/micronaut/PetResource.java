package example.micronaut;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import java.util.List;
import java.util.Optional;

@Path("/pets") // <1>
class PetResource {

    private final PetRepository petRepository;

    PetResource(PetRepository petRepository) { // <2>
        this.petRepository = petRepository;
    }

    @GET // <3>
    List<NameDto> all() { // <4>
        return petRepository.list();
    }

    @GET // <3>
    @Path("/{name}") // <5>
    Optional<Pet> byName(@PathParam("name") String petsName) { // <6>
        return petRepository.findByName(petsName);
    }

}