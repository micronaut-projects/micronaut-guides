package example.micronaut.domain

import groovy.transform.CompileStatic
import io.micronaut.serde.annotation.Serdeable

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import jakarta.validation.constraints.NotNull

import static jakarta.persistence.GenerationType.AUTO

@CompileStatic
@Serdeable
@Entity
@Table(name = 'book')
class Book {

    @Id
    @GeneratedValue(strategy = AUTO)
    Long id

    @NotNull
    @Column(name = 'name', nullable = false)
    String name

    @NotNull
    @Column(name = 'isbn', nullable = false)
    String isbn

    @ManyToOne
    Genre genre

    Book() {}

    Book(@NotNull String isbn,
         @NotNull String name,
         Genre genre) {
        this.isbn = isbn
        this.name = name
        this.genre = genre
    }

    @Override
    String toString() {
        "Book{id=$id, name='$name', isbn='$isbn', genre=$genre}"
    }
}
