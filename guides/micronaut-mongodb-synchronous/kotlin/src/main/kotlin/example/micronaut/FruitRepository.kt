package example.micronaut

import jakarta.validation.Valid

interface FruitRepository {

    fun list(): List<Fruit>

    fun save(@Valid fruit: Fruit) // <1>
}
