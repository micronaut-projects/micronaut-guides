package example.micronaut.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import edu.umd.cs.findbugs.annotations.NonNull;
import edu.umd.cs.findbugs.annotations.Nullable;
import io.micronaut.core.annotation.Introspected;

import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

@Introspected
public class Genre {

    @Nullable
    private Long id;

    @NotNull
    @NonNull
    private String name;

    public Genre() {
    }

    public Genre(@NonNull @NotNull String name) {
        this.name = name;
    }

    @NonNull
    @JsonIgnore
    private Set<Book> books = new HashSet<>();

    @Nullable
    public Long getId() {
        return id;
    }

    public void setId(@Nullable Long id) {
        this.id = id;
    }

    @NonNull
    public String getName() {
        return name;
    }

    public void setName(@NonNull String name) {
        this.name = name;
    }

    @NonNull
    public Set<Book> getBooks() {
        return books;
    }

    public void setBooks(@NonNull Set<Book> books) {
        this.books = books;
    }

    @Override
    public String toString() {
        return "Genre{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", books=" + books +
                '}';
    }
}
