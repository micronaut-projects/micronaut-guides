package example.micronaut

import javax.validation.Valid

interface FruitRepository {

    fun list(): List<Fruit>

    fun save(@Valid fruit: Fruit) // <1>
}
