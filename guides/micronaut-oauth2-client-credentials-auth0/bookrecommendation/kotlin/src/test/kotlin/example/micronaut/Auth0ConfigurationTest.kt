package example.micronaut

import io.micronaut.context.BeanContext
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import jakarta.inject.Inject
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

@MicronautTest(startApplication = false)
internal class Auth0ConfigurationTest {
    @Inject
    lateinit var beanContext: BeanContext
    @Test
    fun testBeanOfTypeAuth0ConfigurationExists() {
        Assertions.assertEquals(
            "https://micronautguides.eu.auth0.com/api/v2/",
            beanContext.getBean(Auth0Configuration::class.java).getApiIdentifier()
        )
    }
}