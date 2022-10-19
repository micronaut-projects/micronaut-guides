package example.micronaut

import groovy.transform.CompileStatic
import io.micronaut.serde.annotation.Serdeable

@CompileStatic
@Serdeable
class GithubUser {

    final String login
    final String name
    final String email

    GithubUser(String login, String name, String email) {
        this.login = login
        this.name = name
        this.email = email
    }
}
