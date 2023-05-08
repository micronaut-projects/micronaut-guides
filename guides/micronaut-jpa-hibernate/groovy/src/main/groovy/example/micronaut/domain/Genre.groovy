package example.micronaut.domain

import com.fasterxml.jackson.annotation.JsonIgnore
import groovy.transform.CompileStatic
import io.micronaut.serde.annotation.Serdeable

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.OneToMany
import jakarta.persistence.Table
import jakarta.validation.constraints.NotNull

import static jakarta.persistence.GenerationType.AUTO

@CompileStatic
@Serdeable
@Entity
@Table(name = 'genre')
class Genre {

    @Id
    @GeneratedValue(strategy = AUTO)
    Long id

    @NotNull
    @Column(name = 'name', nullable = false, unique = true)
    String name

    @JsonIgnore
    @OneToMany(mappedBy = 'genre')
    Set<Book> books = []

    Genre() {}

    Genre(@NotNull String name) {
        this.name = name
    }

    @Override
    String toString() {
        "Genre{id=$id, name='$name'}"
    }
}
