package example.micronaut.resources;

import example.micronaut.domain.Owner;
import example.micronaut.repositories.OwnerRepository;

import javax.validation.constraints.NotBlank;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import java.util.List;
import java.util.Optional;

@Path("/owners") // <1>
class OwnerResource {

    private final OwnerRepository ownerRepository;

    OwnerResource(OwnerRepository ownerRepository) { // <2>
        this.ownerRepository = ownerRepository;
    }

    @GET // <3>
    List<Owner> all() {
        return ownerRepository.findAll(); // <4>
    }

    @GET
    @Path("/{name}") // <5>
    Optional<Owner> byName(@NotBlank @PathParam("name") String ownersName) { // <6>
        return ownerRepository.findByName(ownersName);
    }
}