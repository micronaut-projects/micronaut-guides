package example.micronaut;

import io.micronaut.graal.graalpy.annotations.GraalPyModuleBean;

@GraalPyModuleBean("hello") // <1>
public interface HelloModule {
    String hello(String txt); // <2>
}