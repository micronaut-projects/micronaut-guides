package example.micronaut.domain

import io.micronaut.serde.annotation.Serdeable

import com.fasterxml.jackson.annotation.JsonIgnore
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Table
import javax.persistence.Id
import javax.persistence.OneToMany

@Serdeable
@Entity
@Table(name = "genre")
class Genre(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false, updatable = false)
    val id: Long?,

    @Column(name = "name", nullable = false)
    var name: String
) {

    @JsonIgnore
    @OneToMany(mappedBy = "genre")
    var books: MutableSet<Book> = mutableSetOf()
}