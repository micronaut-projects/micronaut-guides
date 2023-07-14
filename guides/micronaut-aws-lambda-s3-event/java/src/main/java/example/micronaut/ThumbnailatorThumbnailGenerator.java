package example.micronaut;

import io.micronaut.core.annotation.NonNull;
import net.coobird.thumbnailator.Thumbnails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.inject.Singleton;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

@Singleton // <1>
public class ThumbnailatorThumbnailGenerator implements ThumbnailGenerator {
    private static final Logger LOG = LoggerFactory.getLogger(ThumbnailatorThumbnailGenerator.class);
    private final ThumbnailConfiguration thumbnailConfiguration;

    public ThumbnailatorThumbnailGenerator(ThumbnailConfiguration thumbnailConfiguration) { // <2>
        this.thumbnailConfiguration = thumbnailConfiguration;
    }

    @Override
    @NonNull
    public Optional<byte[]> thumbnail(@NonNull InputStream inputStream, @NonNull @NotBlank @Pattern(regexp = "jpg|png") String format) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            Thumbnails.of(inputStream)
                    .size(thumbnailConfiguration.getWidth(), thumbnailConfiguration.getHeight())
                    .outputFormat(format)
                    .toOutputStream(byteArrayOutputStream);
            return Optional.of(byteArrayOutputStream.toByteArray());

        } catch (IOException e) {
            LOG.warn("IOException thrown while generating the thumbnail");
        }
        return Optional.empty();
    }
}
