package example.micronaut

import groovy.transform.CompileStatic
import io.micronaut.core.annotation.Introspected

@Introspected
@CompileStatic
class BintrayPackage {
    String name
    boolean linked
}
