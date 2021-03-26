package example.micronaut;
import edu.umd.cs.findbugs.annotations.NonNull;
import io.micronaut.core.annotation.Introspected;

import javax.validation.constraints.NotBlank;

@Introspected // <1>
public class BookSaved {

    @NotBlank // <2>
    @NonNull // <3>
    private String name;

    @NotBlank // <2>
    @NonNull // <3>
    private String isbn;

    public BookSaved() {

    }

    @NonNull
    public String getName() {
        return name;
    }

    public void setName(@NonNull String name) {
        this.name = name;
    }

    @NonNull
    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(@NonNull String isbn) {
        this.isbn = isbn;
    }
}
