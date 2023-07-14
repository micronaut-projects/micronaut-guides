package example.micronaut.domain

import io.micronaut.serde.annotation.Serdeable

import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Table
import jakarta.persistence.Id
import jakarta.persistence.OneToMany

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