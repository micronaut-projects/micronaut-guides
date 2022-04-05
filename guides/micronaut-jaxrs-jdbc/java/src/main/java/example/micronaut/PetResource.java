package example.micronaut;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Status;

import javax.validation.constraints.NotNull;
import javax.ws.rs.POST;
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

    @POST
    @Status(HttpStatus.CREATED)
    void save(@NonNull @NotNull @Body PetSave petSave) {
        petRepository.save(petSave.getName(), petSave.getType());

    }
}