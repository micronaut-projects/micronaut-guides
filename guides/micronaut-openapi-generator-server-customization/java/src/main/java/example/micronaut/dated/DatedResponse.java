package example.micronaut.dated;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.annotation.Nullable;

import java.time.ZonedDateTime;

/**
 * A response that contains information about last modification and creation date of a resource.
 *
 * @param <T> The response body type.
 */
public final class DatedResponse<T> {

    @Nullable
    private ZonedDateTime created; // <1>

    @Nullable
    private ZonedDateTime lastModified; // <2>

    @NonNull
    private final T body; // <3>

    private DatedResponse(T body, ZonedDateTime created, ZonedDateTime lastModified) {
        this.body = body;
        this.lastModified = lastModified;
    }

    /**
     * Set the created date to this resource.
     *
     * @param created the creation date.
     * @return this response.
     */
    public DatedResponse<T> withCreated(ZonedDateTime created) {
        this.created = created;
        return this;
    }

    /**
     * Set the last modified to this resource.
     *
     * @param lastModified the last modification date.
     * @return this response.
     */
    public DatedResponse<T> withLastModified(ZonedDateTime lastModified) {
        this.lastModified = lastModified;
        return this;
    }

    /**
     * @return The last modification date of returned resource.
     */
    public ZonedDateTime getLastModified() {
        return lastModified;
    }

    /**
     * @return The creation date of returned resource.
     */
    public ZonedDateTime getCreated() {
        return created;
    }

    /**
     * @return The response body.
     */
    public T getBody() {
        return body;
    }

    /**
     * Create a response by specifying only the body.
     *
     * @param body The response body.
     * @return The response.
     * @param <T> The response body type.
     */
    public static <T> DatedResponse<T> of(@NonNull T body) {
        return new DatedResponse<T>(body, null, null);
    }

    /**
     * Create a response by specifying both the body, last modification date and creation date of the resource.
     *
     * @param body The body.
     * @param lastModified The last modification date.
     * @param created The creation date.
     * @return The response.
     * @param <T> The body type.
     */
    public static <T> DatedResponse<T> of(@NonNull T body, @Nullable ZonedDateTime lastModified, @Nullable ZonedDateTime created) {
        return new DatedResponse<T>(body, lastModified, created);
    }
}
