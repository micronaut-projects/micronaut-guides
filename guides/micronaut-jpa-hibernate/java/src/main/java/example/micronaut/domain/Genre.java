package example.micronaut.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.micronaut.serde.annotation.Serdeable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

import static jakarta.persistence.GenerationType.AUTO;

@Serdeable
@Entity
@Table(name = "genre")
public class Genre {

    @Id
    @GeneratedValue(strategy = AUTO)
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @JsonIgnore
    @OneToMany(mappedBy = "genre")
    private Set<Book> books = new HashSet<>();

    public Genre() {}

    public Genre(@NotNull String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Book> getBooks() {
        return books;
    }

    public void setBooks(Set<Book> books) {
        this.books = books;
    }

    @Override
    public String toString() {
        return "Genre{" +
            "id=" + id +
            ", name='" + name + '\'' +
            '}';
    }
}
