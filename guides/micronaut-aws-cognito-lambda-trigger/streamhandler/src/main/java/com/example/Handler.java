package com.example;

import io.micronaut.context.env.Environment;
import io.micronaut.function.aws.MicronautRequestStreamHandler;

public class Handler extends MicronautRequestStreamHandler {
    @Override
    protected String resolveFunctionName(Environment env) {
        return "analytics";
    }
}
