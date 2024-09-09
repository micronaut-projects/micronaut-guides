package example.micronaut;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class WidgetTest {

    @Test
    void widgetBuilder() {
        Widget testWidget = WidgetBuilder.builder()
                .name("foo")
                .id(1)
                .build();
        assertEquals("foo", testWidget.name());
        assertEquals(1, testWidget.id());
    }
}
