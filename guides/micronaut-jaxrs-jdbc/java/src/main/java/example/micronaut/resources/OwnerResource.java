package example.micronaut.resources;

import java.util.List;
import java.util.Optional;

import javax.validation.constraints.NotBlank;
import javax.ws.rs.GET;
import javax.ws.rs.Path;

import example.micronaut.domain.Owner;
import example.micronaut.repositories.OwnerRepository;

@Path("/owners")
class OwnerResource {

    private final OwnerRepository ownerRepository;

    OwnerResource(OwnerRepository ownerRepository) {
        this.ownerRepository = ownerRepository;
    }

    @GET
    List<Owner> all() {
        return ownerRepository.findAll();
    }

    @GET
    @Path("/{name}")
    Optional<Owner> byName(@NotBlank String name) {
        return ownerRepository.findByName(name);
    }
}