package example.micronaut;

import io.micronaut.context.annotation.ConfigurationProperties;
import io.micronaut.core.annotation.NonNull;

@ConfigurationProperties("application") // <1>
public class ApplicationConfigurationProperties implements ApplicationConfiguration {

    protected final Integer DEFAULT_MAX = 10;

    @NonNull
    private Integer max = DEFAULT_MAX;

    @Override
    @NonNull
    public Integer getMax() {
        return max;
    }

    public void setMax(@NonNull Integer max) {
        if(max != null) {
            this.max = max;
        }
    }
}
