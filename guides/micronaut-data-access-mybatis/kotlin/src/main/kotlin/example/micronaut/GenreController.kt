package example.micronaut

import example.micronaut.domain.Genre
import example.micronaut.genre.GenreRepository
import example.micronaut.genre.GenreSaveCommand
import example.micronaut.genre.GenreUpdateCommand
import io.micronaut.http.HttpHeaders.LOCATION
import io.micronaut.http.HttpResponse
import io.micronaut.http.MutableHttpHeaders
import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Delete
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.Post
import io.micronaut.http.annotation.Put
import java.net.URI
import jakarta.validation.Valid

@Controller("/genres") // <1>
open class GenreController(private val genreRepository: GenreRepository) { // <2>

    @Get("/{id}") // <3>
    fun show(id: Long): Genre? = genreRepository.findById(id).orElse(null) // <4>

    @Put // <5>
    open fun update(@Body @Valid command: GenreUpdateCommand): HttpResponse<*> { // <6>
        genreRepository.update(command.id, command.name)
        return HttpResponse
                .noContent<Any>()
                .header(LOCATION, location(command.id).path) // <7>
    }

    @Get(value = "/list{?args*}") // <8>
    open fun list(@Valid args: ListingArguments): List<Genre> = genreRepository.findAll(args)

    @Post // <9>
    open fun save(@Body @Valid cmd: GenreSaveCommand): HttpResponse<Genre> {
        val genre = genreRepository.save(cmd.name)
        return HttpResponse
                .created(genre)
                .headers { headers: MutableHttpHeaders -> headers.location(location(genre.id)) }
    }

    @Delete("/{id}") // <10>
    fun delete(id: Long): HttpResponse<*> {
        genreRepository.deleteById(id)
        return HttpResponse.noContent<Any>()
    }

    private fun location(id: Long?): URI = URI.create("/genres/$id")
}
