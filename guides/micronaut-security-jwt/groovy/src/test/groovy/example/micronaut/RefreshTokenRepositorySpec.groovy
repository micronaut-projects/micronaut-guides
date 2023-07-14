package example.micronaut

import io.micronaut.test.extensions.spock.annotation.MicronautTest
import spock.lang.Specification
import spock.lang.Unroll

import jakarta.inject.Inject
import jakarta.validation.ConstraintViolationException

@MicronautTest
class RefreshTokenRepositorySpec extends Specification {

    @Inject
    RefreshTokenRepository repository

    @Unroll("for RefreshTokenRepository::save parameter #description")
    void 'RefreshTokenRepository::save constraints validation'(String username,
                                                               String refreshToken,
                                                               Boolean revoked,
                                                               String description) {
        when:
        repository.save(username, refreshToken, revoked)

        then:
        thrown(ConstraintViolationException)

        where:
        username | refreshToken | revoked
        null     | 'YYY'        | Boolean.TRUE
        ''       | 'YYY'        | Boolean.TRUE
        'XXX'    | null         | Boolean.TRUE
        'XXX'    | ''           | Boolean.TRUE
        'XXX'    | 'YYY'        | null

        description = username == null ? 'username cannot be null' : (username == '' ? 'username cannot be blank' : (refreshToken == null ? 'refreshToken cannot be null' : (refreshToken == '' ? 'refreshToken cannot be blank' : (revoked == null ? 'revoked cannot be null' : ''))))
    }
}
