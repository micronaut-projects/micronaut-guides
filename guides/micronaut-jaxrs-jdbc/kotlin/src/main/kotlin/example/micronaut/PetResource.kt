package example.micronaut

import io.micronaut.http.HttpStatus
import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Status
import javax.ws.rs.GET
import javax.ws.rs.POST
import javax.ws.rs.Path
import javax.ws.rs.PathParam

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