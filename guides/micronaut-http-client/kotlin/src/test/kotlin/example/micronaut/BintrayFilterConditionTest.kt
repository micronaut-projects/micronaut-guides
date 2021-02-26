package example.micronaut

import io.micronaut.context.ApplicationContext
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class BintrayFilterConditionTest {

    @Test
    fun verifyBintrayFilterIsLoadedIfBintrayUsernameTokenAreSet() {
        val applicationContext = ApplicationContext.run(mapOf<String, Any>(Pair("bintray.username", "john"), Pair("bintray.token", "XXX")))
        assertTrue(applicationContext.containsBean(BintrayFilter::class.java))
        applicationContext.close()
    }

    @Test
    fun verifyBintrayFilterIsNotLoadedIfBintrayUsernameTokenAreSet() {
        val applicationContext = ApplicationContext.run()
        assertFalse(applicationContext.containsBean(BintrayFilter::class.java))
        applicationContext.close()
    }

}