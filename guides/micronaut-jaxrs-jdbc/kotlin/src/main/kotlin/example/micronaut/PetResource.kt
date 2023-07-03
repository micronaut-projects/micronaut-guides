package example.micronaut

import io.micronaut.http.HttpStatus
import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Status
import jakarta.ws.rs.GET
import jakarta.ws.rs.POST
import jakarta.ws.rs.Path
import jakarta.ws.rs.PathParam

@Path("/pets") // <1>
class PetResource(val petRepository: PetRepository) { // <2>

    @GET // <3>
    fun all() = petRepository.findAll() // <4>

    @GET // <3>
    @Path("/{name}") // <4>
    fun byName(@PathParam("name") petsName: String) = petRepository.findByName(petsName) // <5>

    @POST
    @Status(HttpStatus.CREATED)
    fun save(@Body petSave: PetSave) {
        petRepository.save(petSave.name, petSave.type)
    }
}