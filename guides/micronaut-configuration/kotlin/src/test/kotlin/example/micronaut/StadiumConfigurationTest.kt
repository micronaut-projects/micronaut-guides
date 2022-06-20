package example.micronaut

import io.micronaut.context.ApplicationContext
import io.micronaut.inject.qualifiers.Qualifiers
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class StadiumConfigurationTest {
    @Test
    fun testStadiumConfiguration() {
        val items: MutableMap<String, Any> = HashMap()
        items["stadium.fenway.city"] = "Boston" // <1>
        items["stadium.fenway.size"] = 60000
        items["stadium.wrigley.city"] = "Chicago" // <1>
        items["stadium.wrigley.size"] = 45000

        val ctx = ApplicationContext.run(items)

        // <2>
        val fenwayConfiguration = ctx.getBean(StadiumConfiguration::class.java, Qualifiers.byName("fenway"))
        val wrigleyConfiguration = ctx.getBean(StadiumConfiguration::class.java, Qualifiers.byName("wrigley"))

        assertEquals("fenway", fenwayConfiguration.name)
        assertEquals(60000, fenwayConfiguration.size)
        assertEquals("wrigley", wrigleyConfiguration.name)
        assertEquals(45000, wrigleyConfiguration.size)

        ctx.close()
    }
}
