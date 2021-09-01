package example.micronaut

import groovy.transform.CompileStatic
import io.micronaut.core.annotation.Introspected

@CompileStatic
@Introspected
class GithubRepo {

    final String name

    GithubRepo(String name) {
        this.name = name
    }
}
