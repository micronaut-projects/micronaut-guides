package example.micronaut

import io.micronaut.data.jdbc.annotation.JdbcRepository
import io.micronaut.data.model.query.builder.sql.Dialect
import io.micronaut.data.repository.CrudRepository
import java.util.*
import jakarta.validation.constraints.NotBlank

@JdbcRepository(dialect = Dialect.MYSQL) // <1>
interface PetRepository : CrudRepository<Pet, Long> { // <2>
    fun list() : List<NameDto> // <3>

    fun findByName(@NotBlank name: String) : Optional<Pet>

    fun save(@NotBlank name: String, type: PetType) : Pet
}