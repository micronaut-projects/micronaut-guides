package example.micronaut

import groovy.transform.CompileStatic
import io.micronaut.core.annotation.Introspected

import java.time.Month

@CompileStatic
@Introspected
class News {
    Month month
    List<String> headlines
}
