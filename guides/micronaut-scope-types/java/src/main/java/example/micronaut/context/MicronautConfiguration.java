package example.micronaut.context;

import io.micronaut.context.annotation.ConfigurationProperties;
import io.micronaut.context.annotation.Context;
import jakarta.validation.constraints.Pattern;

@Context // <1>
@ConfigurationProperties("micronaut") // <2>
public class MicronautConfiguration {

    @Pattern(regexp = "groovy|java|kotlin") // <3>
    private String language;

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }
}
