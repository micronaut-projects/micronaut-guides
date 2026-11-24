package example.micronaut

import groovy.transform.CompileStatic
import jakarta.inject.Singleton

@CompileStatic
@Singleton // <1>
class ScientificCalculator implements Calculator {

    @Override
    int add(int a, int b) {
        Math.addExact(a, b)
    }
}
