package example.micronaut

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test


class WidgetTest {

    @Test
    fun widgetBuilder() {
        val testWidget = WidgetBuilder.builder()
            .name("foo")
            .id(1)
            .build()
        assertEquals("foo", testWidget.name)
        assertEquals(1, testWidget.id)
    }
}
