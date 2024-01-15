/*
 * Copyright 2017-2024 original authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
