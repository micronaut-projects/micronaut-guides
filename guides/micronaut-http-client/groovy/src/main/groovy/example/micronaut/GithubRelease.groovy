package example.micronaut

import groovy.transform.CompileStatic
import io.micronaut.core.annotation.Introspected

@Introspected
@CompileStatic
class GithubRelease {
    String name
    String url
}
