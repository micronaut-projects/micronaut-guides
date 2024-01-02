package example.micronaut
/*
//tag::package[]
package example.micronaut
//tag::package[]
*/
//tag::imports[]

import io.micronaut.context.ApplicationContext
import io.micronaut.context.exceptions.BeanInstantiationException
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class ContextTest {

    @Test
    fun lifeCycleOfClassesAnnotatedWithAtContextIsBoundToThatOfTheBeanContext() {

        val thrown =
            assertThrows<BeanInstantiationException> { ApplicationContext.run(mapOf("micronaut.language" to "scala")) }
        assertTrue(thrown.message!!.contains("must match \"groovy|java|kotlin\""))
    }
}