package example.micronaut

import io.micronaut.core.annotation.Creator
import io.micronaut.serde.annotation.Serdeable
import org.bson.codecs.pojo.annotations.BsonCreator
import org.bson.codecs.pojo.annotations.BsonProperty
import jakarta.validation.constraints.NotBlank

@Serdeable // <1>
data class Fruit @Creator @BsonCreator constructor( // <4>
    @field:BsonProperty("name") @param:BsonProperty("name") @field:NotBlank val name: String, // <2> <3>
    @field:BsonProperty("description") @param:BsonProperty("description") var description: String?) { // <3>

    constructor(name: String) : this(name, null)
}
