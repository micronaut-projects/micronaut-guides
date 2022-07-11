package example.micronaut

import io.micronaut.context.annotation.Replaces
import io.micronaut.context.annotation.Requires
import jakarta.inject.Singleton

@Requires(property = "spec.name", value = "FruitControllerValidationTest")
@Singleton
@Replaces(FruitRepository::class)
internal class MockFruitRepository : FruitRepository {

    override fun list(): List<Fruit> = emptyList()

    override fun save(fruit: Fruit) {}
}
