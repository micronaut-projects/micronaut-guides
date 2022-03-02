package example.micronaut.model;

import io.micronaut.core.annotation.Introspected;

@Introspected // <1>
public class WordFrequency {
    private final String word;
    private final Integer numberOccurred; // <2>

    public WordFrequency(String word, Integer numberOccurred) {
        this.word = word;
        this.numberOccurred = numberOccurred;
    }

    public String getWord() {
        return word;
    }

    public Integer getNumberOccurred() {
        return numberOccurred;
    }
}