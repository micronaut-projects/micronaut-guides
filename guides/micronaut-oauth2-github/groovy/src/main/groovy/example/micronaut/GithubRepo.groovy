package example.micronaut

import groovy.transform.CompileStatic
import io.micronaut.core.annotation.Creator
import io.micronaut.core.annotation.Introspected

@CompileStatic
@Introspected
class GithubRepo {

    final String name

    @Creator
    GithubRepo(String name) {
        this.name = name
    }
}
