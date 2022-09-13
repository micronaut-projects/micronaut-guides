package example.micronaut;

import io.micronaut.core.annotation.NonNull;
import jakarta.inject.Singleton;

@Singleton
public class MessageService {

    @NonNull
    public String compose() {
        return "Hello World";
    }
}
