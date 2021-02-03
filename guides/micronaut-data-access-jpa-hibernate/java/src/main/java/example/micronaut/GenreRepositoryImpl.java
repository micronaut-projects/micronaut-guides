//tag::package[]
package example.micronaut;
//end::package[]
//tag::import[]

//end::import[]
//tag::importContent[]
import example.micronaut.domain.Genre;
import javax.inject.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import io.micronaut.transaction.annotation.ReadOnly;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
//end::importContent[]
//tag::clazz[]
@Singleton // <1>
public class GenreRepositoryImpl implements GenreRepository {
//end::clazz[]
//tag::clazzContent[]
    private final EntityManager entityManager;  // <2>
    private final ApplicationConfiguration applicationConfiguration;

    public GenreRepositoryImpl(EntityManager entityManager,
                               ApplicationConfiguration applicationConfiguration) { // <2>
        this.entityManager = entityManager;
        this.applicationConfiguration = applicationConfiguration;
    }

    @Override
//end::clazzContent[]
//tag::findById[]
    @ReadOnly  // <3>
    public Optional<Genre> findById(@NotNull Long id) {
//end::findById[]
//tag::findByIdContent[]
        return Optional.ofNullable(entityManager.find(Genre.class, id));
    }

    @Override
//end::findByIdContent[]
//tag::save[]
    @Transactional  // <4>
    public Genre save(@NotBlank String name) {
//end::save[]
//tag::saveContent[]
        Genre genre = new Genre(name);
        entityManager.persist(genre);
        return genre;
    }

    @Override
//end::saveContent[]
//tag::deleteById[]
    @Transactional // <4>
    public void deleteById(@NotNull Long id) {
//end::deleteById[]
//tag::deleteByIdContent[]
        findById(id).ifPresent(entityManager::remove);
    }

    private final static List<String> VALID_PROPERTY_NAMES = Arrays.asList("id", "name");

//end::deleteByIdContent[]
//tag::findAll[]
    @ReadOnly // <3>
    public List<Genre> findAll(@NotNull SortingAndOrderArguments args) {
//end::findAll[]
//tag::findAllContent[]
        String qlString = "SELECT g FROM Genre as g";
        if (args.getOrder().isPresent() && args.getSort().isPresent() && VALID_PROPERTY_NAMES.contains(args.getSort().get())) {
                qlString += " ORDER BY g." + args.getSort().get() + " " + args.getOrder().get().toLowerCase();
        }
        TypedQuery<Genre> query = entityManager.createQuery(qlString, Genre.class);
        query.setMaxResults(args.getMax().orElseGet(applicationConfiguration::getMax));
        args.getOffset().ifPresent(query::setFirstResult);

        return query.getResultList();
    }

    @Override
//end::findAllContent[]
//tag::update[]
    @Transactional // <4>
    public int update(@NotNull Long id, @NotBlank String name) {
//end::update[]
//tag::updateContent[]
        return entityManager.createQuery("UPDATE Genre g SET name = :name where id = :id")
                .setParameter("name", name)
                .setParameter("id", id)
                .executeUpdate();
    }

    @Override // <4>
//end::updateContent[]
//tag::saveWithException[]
    @Transactional // <4>
    public Genre saveWithException(@NotBlank String name) {
//end::saveWithException[]
//tag::saveWithExceptionContent[]
        save(name);
        throw new PersistenceException();
    }
}
//end::saveWithExceptionContent[]
