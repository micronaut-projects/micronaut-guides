package example.micronaut;
import edu.umd.cs.findbugs.annotations.NonNull;
import io.micronaut.core.annotation.Introspected;
import javax.validation.constraints.NotBlank;

@Introspected // <1>
public class Book {

    @NotBlank // <2>
    @NonNull // <3>
    private String name;

    public Book() {
    }

    @NonNull
    public String getName() {
        return name;
    }

    public void setName(@NonNull String name) {
        this.name = name;
    }
}
