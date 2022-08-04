package example.micronaut;

import com.github.ksuid.Ksuid;
import io.micronaut.context.annotation.Requires;
import jakarta.inject.Singleton;

@Requires(classes = Ksuid.class)
@Singleton
public class KsuidGenerator implements IdGenerator {

    @Override
    public String generate() {
        return Ksuid.newKsuid().toString();
    }
}


