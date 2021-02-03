package example.micronaut

import io.micronaut.context.ApplicationContext
import io.micronaut.inject.qualifiers.Qualifiers
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class StadiumConfigurationTest {
    @Test
    fun testStadiumConfiguration() {
        val items: MutableMap<String, Any> = HashMap()
        items["stadium.fenway.city"] = "Boston" // <1>
        items["stadium.fenway.size"] = 60000
        items["stadium.wrigley.city"] = "Chicago" // <1>
        items["stadium.wrigley.size"] = 45000
        val ctx = ApplicationContext.run(ApplicationContext::class.java, items)
        // <2>
        val fenwayConfiguration = ctx.getBean(StadiumConfiguration::class.java, Qualifiers.byName("fenway"))
        val wrigleyConfiguration = ctx.getBean(StadiumConfiguration::class.java, Qualifiers.byName("wrigley"))
        Assertions.assertEquals("fenway", fenwayConfiguration.name)
        Assertions.assertEquals(60000, fenwayConfiguration.size)
        Assertions.assertEquals("wrigley", wrigleyConfiguration.name)
        Assertions.assertEquals(45000, wrigleyConfiguration.size)
        ctx.close()
    }
}