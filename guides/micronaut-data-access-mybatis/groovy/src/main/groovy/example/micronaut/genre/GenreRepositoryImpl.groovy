package example.micronaut.genre

import example.micronaut.ListingArguments
import example.micronaut.domain.Genre
import groovy.transform.CompileStatic
import io.micronaut.core.annotation.NonNull
import jakarta.inject.Singleton

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull

@CompileStatic
@Singleton // <1>
class GenreRepositoryImpl implements GenreRepository {

    private final GenreMapper genreMapper

    GenreRepositoryImpl(GenreMapper genreMapper) {
        this.genreMapper = genreMapper
    }

    @Override
    @NonNull
    Optional<Genre> findById(long id) {
        Optional.ofNullable(genreMapper.findById(id))
    }

    @Override
    @NonNull
    Genre save(@NonNull @NotBlank String name) {
        Genre genre = new Genre(name)
        genreMapper.save(genre)
        genre
    }

    @Override
    void deleteById(long id) {
        findById(id).ifPresent(genre -> genreMapper.deleteById(id))
    }

    @NonNull
    List<Genre> findAll(@NonNull @NotNull ListingArguments args) {

        if (args.max != null && args.sort != null && args.offset != null && args.order != null) {
            return genreMapper.findAllByOffsetAndMaxAndSortAndOrder(
                    args.offset,
                    args.max,
                    args.sort,
                    args.order)
        }

        if (args.max != null && args.offset!= null && (args.sort == null || args.order == null)) {
            return genreMapper.findAllByOffsetAndMax(args.offset, args.max)
        }

        if ((args.max == null || args.offset == null) && args.sort != null && args.order !=null) {
            return genreMapper.findAllBySortAndOrder(args.sort, args.order)
        }

        genreMapper.findAll()
    }

    @Override
    int update(long id, @NonNull @NotBlank String name) {
        genreMapper.update(id, name)
        -1
    }
}
