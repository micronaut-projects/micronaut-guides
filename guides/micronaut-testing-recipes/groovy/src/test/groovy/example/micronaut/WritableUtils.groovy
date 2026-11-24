package example.micronaut

import groovy.transform.CompileStatic
import io.micronaut.core.annotation.NonNull
import io.micronaut.core.io.Writable

@CompileStatic
final class WritableUtils {

    private WritableUtils() {
    }

    @NonNull
    static String writableToString(@NonNull Writable writable) throws IOException {
        StringWriter stringWriter = new StringWriter()
        writable.writeTo(stringWriter)
        stringWriter.toString()
    }
}
