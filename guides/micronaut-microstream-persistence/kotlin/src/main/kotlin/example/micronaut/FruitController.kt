package example.micronaut

import io.micronaut.core.annotation.NonNull
import io.micronaut.http.HttpStatus
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Delete
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.PathVariable
import io.micronaut.http.annotation.Post
import io.micronaut.http.annotation.Put
import io.micronaut.http.annotation.Status
import jakarta.inject.Inject
import javax.validation.Valid

@Controller("/fruits") // <1>
internal class FruitController(
    @Inject private val fruitRepository: FruitRepository // <2>
) {

    @Get // <3>
    fun list() = fruitRepository.list()

    @Post // <4>
    @Status(HttpStatus.CREATED) // <5>
    fun create(
        fruit: @Valid FruitCommand // <6>
    ) = fruitRepository.create(fruit)

    @Put
    fun update(fruit: @Valid FruitCommand) = fruitRepository.update(fruit)

    @Get("/{name}") // <7>
    fun find(@PathVariable name: String): Fruit? = fruitRepository.find(name)

    @Delete
    fun delete(fruit: @Valid FruitCommand) = fruitRepository.delete(fruit)
}