package example.micronaut;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Status;

import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import java.util.List;
import java.util.Optional;

@Path("/pets") // <1>
class PetResource {

    private final PetRepository petRepository;

    PetResource(PetRepository petRepository) { // <2>
        this.petRepository = petRepository;
    }

    @GET // <3>
    public List<NameDto> all() { // <4>
        return petRepository.list();
    }

    @GET // <3>
    @Path("/{name}") // <5>
    public Optional<Pet> byName(@PathParam("name") String petsName) { // <6>
        return petRepository.findByName(petsName);
    }

    @POST
    @Status(HttpStatus.CREATED)
    public void save(@NonNull @NotNull @Body PetSave petSave) {
        petRepository.save(petSave.getName(), petSave.getType());

    }
}