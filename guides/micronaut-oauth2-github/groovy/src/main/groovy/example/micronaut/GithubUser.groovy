package example.micronaut

import groovy.transform.CompileStatic
import io.micronaut.core.annotation.Creator
import io.micronaut.core.annotation.Introspected

@CompileStatic
@Introspected
class GithubUser {

    final String login
    final String name
    final String email

    @Creator
    GithubUser(String login, String name, String email) {
        this.login = login
        this.name = name
        this.email = email
    }
}
