package example.micronaut;

import io.micronaut.context.annotation.ConfigurationProperties;

@ConfigurationProperties("application") // <1>
public class ApplicationConfigurationProperties implements ApplicationConfiguration {

    private final int DEFAULT_MAX = 10;

    private int max = DEFAULT_MAX;

    @Override
    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }
}
