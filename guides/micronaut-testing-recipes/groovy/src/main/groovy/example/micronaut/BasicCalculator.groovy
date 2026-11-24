package example.micronaut

import groovy.transform.CompileStatic
import io.micronaut.context.annotation.Primary
import jakarta.inject.Singleton

@CompileStatic
@Primary // <1>
@Singleton // <2>
class BasicCalculator implements Calculator {

    @Override
    int add(int a, int b) {
        a + b
    }
}
