package example.micronaut;

import com.github.ksuid.Ksuid;
import io.micronaut.context.annotation.Requires;
import jakarta.inject.Singleton;

@Requires(classes = Ksuid.class) // <1>
@Singleton // <2>
public class KsuidGenerator implements IdGenerator {

    @Override
    public String generate() {
        return Ksuid.newKsuid().toString();
    }
}


