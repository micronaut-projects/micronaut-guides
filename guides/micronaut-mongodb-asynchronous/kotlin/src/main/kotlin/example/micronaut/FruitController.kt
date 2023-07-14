package example.micronaut

import io.micronaut.http.HttpStatus
import io.micronaut.http.HttpStatus.CONFLICT
import io.micronaut.http.HttpStatus.CREATED
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.Post
import org.reactivestreams.Publisher
import reactor.core.publisher.Mono
import jakarta.validation.Valid

@Controller("/fruits") // <1>
open class FruitController(private val fruitService: FruitRepository) { // <2>

    @Get // <3>
    fun list(): Publisher<Fruit> = fruitService.list()

    @Post // <4>
    open fun save(@Valid fruit: Fruit): Mono<HttpStatus> { // <5>
        return fruitService.save(fruit) // <6>
                .map { added: Boolean -> if (added) CREATED else CONFLICT }
    }
}
