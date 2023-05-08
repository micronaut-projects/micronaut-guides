package example.micronaut.domain

import io.micronaut.serde.annotation.Serdeable

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Table
import jakarta.persistence.Id
import jakarta.persistence.ManyToOne

@Serdeable
@Entity
@Table(name = "book")
class Book(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Long,

    @Column(name = "name", nullable = false)
    var name: String,

    @Column(name = "isbn", nullable = false)
    var isbn: String
) {
    @ManyToOne
    var genre: Genre? = null
}