package example.micronaut

import groovy.transform.CompileStatic
import io.micronaut.core.annotation.Introspected

import java.time.Month

@CompileStatic
@Introspected // <1>
class News implements Serializable { // <2>
    Month month
    List<String> headlines
}
