package example.micronaut

import groovy.transform.CompileStatic
import io.micronaut.serde.annotation.Serdeable

import java.time.Month

@CompileStatic
@Serdeable // <1>
class News {
    Month month
    List<String> headlines
}
