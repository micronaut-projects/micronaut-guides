package example.micronaut

import io.micronaut.http.HttpStatus.CREATED
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.Post
import io.micronaut.http.annotation.Status
import io.micronaut.scheduling.TaskExecutors
import io.micronaut.scheduling.annotation.ExecuteOn
import jakarta.validation.Valid

@Controller("/fruits") // <1>
@ExecuteOn(TaskExecutors.IO) // <2>
open class FruitController(private val fruitService: FruitRepository) { // <3>

    @Get // <4>
    fun list(): List<Fruit> = fruitService.list()

    @Post // <5>
    @Status(CREATED) // <6>
    open fun save(@Valid fruit: Fruit) = // <7>
        fruitService.save(fruit)
}
