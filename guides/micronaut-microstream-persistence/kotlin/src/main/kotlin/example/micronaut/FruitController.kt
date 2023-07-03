package example.micronaut

import io.micronaut.http.HttpStatus
import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Delete
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.PathVariable
import io.micronaut.http.annotation.Post
import io.micronaut.http.annotation.Put
import io.micronaut.http.annotation.Status
import io.micronaut.scheduling.TaskExecutors.IO
import io.micronaut.scheduling.annotation.ExecuteOn
import jakarta.inject.Inject
import jakarta.validation.Valid

@Controller("/fruits") // <1>
open class FruitController {

    @Inject
    private lateinit var fruitRepository: FruitRepository // <2>

    @Get // <3>
    fun list() = fruitRepository.list()

    @ExecuteOn(IO)
    @Post // <4>
    @Status(HttpStatus.CREATED) // <5>
    open fun create(@Valid @Body fruit: FruitCommand) // <6>
        = fruitRepository.create(fruit)

    @Put
    open fun update(@Valid @Body fruit: FruitCommand) = fruitRepository.update(fruit)

    @Get("/{name}") // <7>
    fun find(@PathVariable name: String): Fruit? = fruitRepository.find(name)

    @ExecuteOn(IO)
    @Delete
    @Status(HttpStatus.NO_CONTENT)
    open fun delete(@Valid @Body fruit: FruitCommand) = fruitRepository.delete(fruit)
}