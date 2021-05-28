package example.micronaut;

import io.micronaut.context.annotation.ConfigurationProperties;
import io.micronaut.core.annotation.NonNull;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@ConfigurationProperties("thumbnail") // <1>
public class ThumbnailConfigurationProperties implements ThumbnailConfiguration {

    @NonNull
    @NotNull // <2>
    private Integer width;

    @NonNull
    @NotNull // <2>
    private Integer height;

    @Override
    @NonNull
    public Integer getWidth() {
        return width;
    }

    public void setWidth(@NonNull Integer width) {
        this.width = width;
    }

    @Override
    @NonNull
    public Integer getHeight() {
        return height;
    }

    public void setHeight(@NonNull Integer height) {
        this.height = height;
    }
}
