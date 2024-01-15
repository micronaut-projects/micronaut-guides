package example.micronaut;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import io.micronaut.core.annotation.Introspected;

@Introspected // <1>
@JacksonXmlRootElement(localName = "book") // <2>
public record Book(@JacksonXmlProperty(isAttribute = false) String name,
                   @JacksonXmlProperty(isAttribute = true) String isbn) { // <3>
}
