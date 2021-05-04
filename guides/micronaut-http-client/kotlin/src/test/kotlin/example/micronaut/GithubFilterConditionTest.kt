package example.micronaut

import io.micronaut.context.ApplicationContext
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class GithubFilterConditionTest {

    @Test
    fun verifyBintrayFilterIsLoadedIfBintrayUsernameTokenAreSet() {
        val applicationContext = ApplicationContext.run(mapOf<String, Any>(Pair("github.username", "john"), Pair("github.token", "XXX")))
        assertTrue(applicationContext.containsBean(GithubFilter::class.java))
        applicationContext.close()
    }

    @Test
    fun verifyBintrayFilterIsNotLoadedIfBintrayUsernameTokenAreSet() {
        val applicationContext = ApplicationContext.run()
        assertFalse(applicationContext.containsBean(GithubFilter::class.java))
        applicationContext.close()
    }
}
