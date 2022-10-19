package example.micronaut

import groovy.transform.CompileStatic
import io.micronaut.serde.annotation.Serdeable

@CompileStatic
@Serdeable
class GithubRepo {

    final String name

    GithubRepo(String name) {
        this.name = name
    }
}
