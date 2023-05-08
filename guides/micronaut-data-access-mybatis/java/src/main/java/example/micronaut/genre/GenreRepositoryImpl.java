package example.micronaut.genre;

import example.micronaut.ListingArguments;
import example.micronaut.domain.Genre;
import io.micronaut.core.annotation.NonNull;
import jakarta.inject.Singleton;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;

@Singleton // <1>
public class GenreRepositoryImpl implements GenreRepository {

    private final GenreMapper genreMapper;

    public GenreRepositoryImpl(GenreMapper genreMapper) {
        this.genreMapper = genreMapper;
    }

    @Override
    @NonNull
    public Optional<Genre> findById(long id) {
        return Optional.ofNullable(genreMapper.findById(id));
    }

    @Override
    @NonNull
    public Genre save(@NonNull @NotBlank String name) {
        Genre genre = new Genre(name);
        genreMapper.save(genre);
        return genre;
    }

    @Override
    public void deleteById(long id) {
        findById(id).ifPresent(genre -> genreMapper.deleteById(id));
    }

    @NonNull
    public List<Genre> findAll(@NonNull @NotNull ListingArguments args) {

        if (args.getMax() != null && args.getSort() != null && args.getOffset() != null && args.getOrder() != null) {
            return genreMapper.findAllByOffsetAndMaxAndSortAndOrder(
                    args.getOffset(),
                    args.getMax(),
                    args.getSort(),
                    args.getOrder());
        }

        if (args.getMax() != null && args.getOffset()!= null && (args.getSort() == null || args.getOrder() == null)) {
            return genreMapper.findAllByOffsetAndMax(args.getOffset(), args.getMax());
        }

        if ((args.getMax() == null || args.getOffset() == null) && args.getSort() != null && args.getOrder() !=null) {
            return genreMapper.findAllBySortAndOrder(args.getSort(), args.getOrder());
        }

        return genreMapper.findAll();
    }

    @Override
    public int update(long id, @NonNull @NotBlank String name) {
        genreMapper.update(id, name);
        return -1;
    }
}
