package example.micronaut

import example.micronaut.domain.Genre
import io.micronaut.transaction.annotation.ReadOnly
import jakarta.inject.Singleton

import javax.persistence.EntityManager
import javax.persistence.PersistenceException
import javax.persistence.TypedQuery
import javax.transaction.Transactional
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

@Singleton // <1>
class GenreRepositoryImpl implements GenreRepository {

    private static final List<String> VALID_PROPERTY_NAMES = ['id', 'name']

    private final EntityManager entityManager  // <2>
    private final ApplicationConfiguration applicationConfiguration

    GenreRepositoryImpl(EntityManager entityManager,
                        ApplicationConfiguration applicationConfiguration) { // <2>
        this.entityManager = entityManager
        this.applicationConfiguration = applicationConfiguration
    }

    @Override
    @ReadOnly  // <3>
    Optional<Genre> findById(long id) {
        Optional.ofNullable(entityManager.find(Genre, id))
    }

    @Override
    @Transactional // <4>
    Genre save(@NotBlank String name) {
        Genre genre = new Genre(name)
        entityManager.persist(genre)
        genre
    }

    @Override
    @Transactional // <4>
    void deleteById(long id) {
        findById(id).ifPresent(entityManager::remove)
    }

    @ReadOnly // <3>
    List<Genre> findAll(@NotNull SortingAndOrderArguments args) {
        String qlString = 'SELECT g FROM Genre as g'
        if (args.order.present && args.sort.present && VALID_PROPERTY_NAMES.contains(args.sort.get())) {
            qlString += ' ORDER BY g.' + args.sort.get() + ' ' + args.order.get().toLowerCase()
        }
        TypedQuery<Genre> query = entityManager.createQuery(qlString, Genre)
        query.maxResults = args.max.orElseGet(applicationConfiguration::getMax)
        args.offset.ifPresent(query::setFirstResult)

        query.resultList
    }

    @Override
    @Transactional // <4>
    int update(long id, @NotBlank String name) {
        entityManager.createQuery('UPDATE Genre g SET name = :name where id = :id')
                .setParameter('name', name)
                .setParameter('id', id)
                .executeUpdate()
    }

    @Override // <4>
    @Transactional // <4>
    Genre saveWithException(@NotBlank String name) {
        save(name)
        throw new PersistenceException()
    }
}
