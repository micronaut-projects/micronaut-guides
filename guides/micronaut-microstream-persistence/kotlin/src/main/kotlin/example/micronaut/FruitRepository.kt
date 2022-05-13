package example.micronaut

internal interface FruitRepository {

    fun list(): Collection<Fruit>

    fun create(fruit: FruitCommand): Fruit

    fun update(fruit: FruitCommand): Fruit?

    fun find(name: String): Fruit?

    fun delete(fruit: FruitCommand)
}