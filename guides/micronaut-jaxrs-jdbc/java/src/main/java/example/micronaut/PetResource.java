/*
 * Copyright 2017-2024 original authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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