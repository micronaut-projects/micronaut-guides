package example.micronaut.domain

import com.fasterxml.jackson.annotation.JsonIgnore
import groovy.transform.CompileStatic
import io.micronaut.serde.annotation.Serdeable

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.OneToMany
import javax.persistence.Table
import jakarta.validation.constraints.NotNull

import static javax.persistence.GenerationType.AUTO

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
