package example.micronaut;

import io.micronaut.context.env.Environment;
import io.micronaut.core.annotation.Introspected;
import io.micronaut.function.aws.MicronautRequestStreamHandler;

@Introspected
public class FunctionRequestHandler extends MicronautRequestStreamHandler { // <1>

    @Override
    protected String resolveFunctionName(Environment env) {
        return "requestfunction"; // <2>
    }
}
