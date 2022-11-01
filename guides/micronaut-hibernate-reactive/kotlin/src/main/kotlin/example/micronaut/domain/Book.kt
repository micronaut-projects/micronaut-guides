package example.micronaut.domain

import io.micronaut.serde.annotation.Serdeable

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Table
import javax.persistence.Id
import javax.persistence.ManyToOne

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