package example.micronaut;

import io.micronaut.data.annotation.GeneratedValue;
import io.micronaut.data.annotation.Id;
import io.micronaut.data.annotation.MappedEntity;
import io.micronaut.serde.annotation.Serdeable;

import static io.micronaut.data.annotation.GeneratedValue.Type.AUTO;

@Serdeable
@MappedEntity // <1>
public class Book {

    @Id // <2>
    @GeneratedValue(AUTO) // <3>
    private Long id;

    private String name;
    private String isbn;

    public Book(String isbn, String name) {
        this.isbn = isbn;
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

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }
}
