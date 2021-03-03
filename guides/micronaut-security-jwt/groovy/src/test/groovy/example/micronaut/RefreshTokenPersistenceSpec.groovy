package example.micronaut

import io.micronaut.context.ApplicationContext
import io.micronaut.security.token.refresh.RefreshTokenPersistence
import spock.lang.AutoCleanup
import spock.lang.Shared
import spock.lang.Specification

class RefreshTokenPersistenceSpec extends Specification {

    @AutoCleanup
    @Shared
    ApplicationContext applicationContext = ApplicationContext.run()

    void "RefreshTokenPersistence bean exists"() {
        expect:
        applicationContext.containsBean(RefreshTokenPersistence)
    }
}
