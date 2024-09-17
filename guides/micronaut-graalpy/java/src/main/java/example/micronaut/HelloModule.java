package example.micronaut;

import io.micronaut.graal.graalpy.annotations.GraalPyModule;

@GraalPyModule("hello") // <1>
public interface HelloModule {
    String hello(String txt); // <2>
}