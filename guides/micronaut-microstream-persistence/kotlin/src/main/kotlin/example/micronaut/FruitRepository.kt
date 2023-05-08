package example.micronaut

import jakarta.validation.Valid

interface FruitRepository {

    fun list(): Collection<Fruit>

    fun create(@Valid fruit: FruitCommand): Fruit // <1>

    fun update(@Valid fruit: FruitCommand): Fruit? // <1>

    fun find(name: String): Fruit?

    fun delete(@Valid fruit: FruitCommand) // <1>
}