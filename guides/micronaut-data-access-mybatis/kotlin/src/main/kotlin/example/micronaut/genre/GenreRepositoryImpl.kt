package example.micronaut.genre

import example.micronaut.ListingArguments
import example.micronaut.domain.Genre
import io.micronaut.core.annotation.NonNull
import jakarta.inject.Singleton
import java.util.Optional
import javax.validation.constraints.NotBlank

@Singleton // <1>
open class GenreRepositoryImpl(private val genreMapper: GenreMapper) : GenreRepository {

    override fun findById(id: Long): Optional<Genre> =
        Optional.ofNullable(genreMapper.findById(id))

    @NonNull
    override fun save(@NotBlank name: String): Genre {
        val genre = Genre(name)
        genreMapper.save(genre)
        return genre
    }

    override fun deleteById(id: Long) {
        findById(id).ifPresent { genreMapper.deleteById(id) }
    }

    @NonNull
    override fun findAll(args: ListingArguments): List<Genre> {
        if (args.getMax().isPresent && args.getSort().isPresent && args.getOffset().isPresent && args.getOrder().isPresent) {
            return genreMapper.findAllByOffsetAndMaxAndSortAndOrder(
                    args.getOffset().get(),
                    args.getMax().get(),
                    args.getSort().get(),
                    args.getOrder().get())
        }

        if (args.getMax().isPresent && args.getOffset().isPresent && (!args.getSort().isPresent || !args.getOrder().isPresent)) {
            return genreMapper.findAllByOffsetAndMax(args.getOffset().get(), args.getMax().get())
        }

        if ((!args.getMax().isPresent || !args.getOffset().isPresent) && args.getSort().isPresent && args.getOrder().isPresent) {
            return genreMapper.findAllBySortAndOrder(args.getSort().get(), args.getOrder().get())
        }

        return genreMapper.findAll()
    }

    override fun update(id: Long, @NotBlank name: String): Int {
        genreMapper.update(id, name)
        return -1
    }
}
