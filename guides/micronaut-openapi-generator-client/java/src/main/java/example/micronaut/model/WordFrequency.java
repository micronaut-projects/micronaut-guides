package example.micronaut.model;

import io.micronaut.core.annotation.Introspected;
import io.micronaut.core.annotation.NonNull;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Introspected // <1>
public class WordFrequency {
    @NonNull @NotBlank
    private final String word;
    @NonNull @NotNull
    private final Integer numberOccurred; // <2>

    public WordFrequency(@NonNull String word, @NonNull Integer numberOccurred) {
        this.word = word;
        this.numberOccurred = numberOccurred;
    }

    @NonNull
    public String getWord() {
        return word;
    }

    @NonNull
    public Integer getNumberOccurred() {
        return numberOccurred;
    }
}