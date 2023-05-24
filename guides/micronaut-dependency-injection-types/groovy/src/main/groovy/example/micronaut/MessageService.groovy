package example.micronaut

import io.micronaut.core.annotation.NonNull
import jakarta.inject.Singleton

@Singleton
class MessageService {

    @NonNull
    String compose() {
        "Hello World"
    }
}
