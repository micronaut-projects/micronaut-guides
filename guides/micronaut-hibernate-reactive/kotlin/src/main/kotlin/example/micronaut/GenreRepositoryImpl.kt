package example.micronaut

import example.micronaut.domain.Genre
import jakarta.inject.Singleton
import org.hibernate.SessionFactory
import org.hibernate.reactive.stage.Stage
import org.reactivestreams.Publisher
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import jakarta.persistence.PersistenceException


@Singleton // <1>
open class GenreRepositoryImpl(
    val applicationConfiguration: ApplicationConfiguration, // <2>
    sessionFactory: SessionFactory, // <3>
) : GenreRepository {
    private val sessionFactory: Stage.SessionFactory
    private val VALID_PROPERTY_NAMES = arrayOf("id", "name")

    init {
        this.sessionFactory = sessionFactory.unwrap(Stage.SessionFactory::class.java)
    }

    override fun findById(id: Long): Publisher<Genre?> {
        return Mono.fromCompletionStage(sessionFactory.withTransaction { session -> // <4>
            session.find(Genre::class.java, id)
        })
    }

    override fun save(name: String): Publisher<Genre> {
        return Mono.fromCompletionStage(sessionFactory.withTransaction { session ->
            val entity = Genre(id = null, name = name)
            session.persist(entity)
                .thenApply { entity }
        })
    }

    override fun update(id: Long, name: String?): Publisher<Int?> {
        return Mono.fromCompletionStage(sessionFactory.withTransaction { session ->
            session.createQuery<Long>("UPDATE Genre g SET name = :name where id = :id")
                .setParameter("name", name)
                .setParameter("id", id)
                .executeUpdate()
        })
    }

    override fun findAll(args: SortingAndOrderArguments): Publisher<Genre?> {
        val qlString: String = createQuery(args)
        return Mono.fromCompletionStage(sessionFactory.withSession { session ->
            val query = session.createQuery(
                qlString,
                Genre::class.java
            )
            query.maxResults = args.max ?: applicationConfiguration.max
            args.offset?.let { query.setFirstResult(it) }
            query.resultList
        })
            .flatMapMany { Flux.fromIterable(it) }
    }

    private fun createQuery(args: SortingAndOrderArguments): String {
        var qlString = "SELECT g FROM Genre as g"
        val order = args.order
        val sort = args.sort
        if (order != null && sort != null && VALID_PROPERTY_NAMES.contains(sort)) {
            qlString += " ORDER BY g." + sort + ' ' + order.lowercase()
        }
        return qlString
    }

    override fun deleteById(id: Long) {
        sessionFactory.withTransaction { session ->
            session.find(Genre::class.java, id).thenApply { entity ->
                session.remove(entity)
            }
        }
    }

    override fun saveWithException(name: String): Publisher<Genre?> {
        return Mono.fromCompletionStage(sessionFactory.withTransaction { session ->
            val entity = Genre(id = null, name = name)
            session.persist(entity)
                .thenApply<Genre> { throw PersistenceException() }
        })
    }
}