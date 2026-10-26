package example.micronaut

import io.micronaut.core.io.ResourceLoader
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import jakarta.inject.Inject
import spock.lang.Specification

@MicronautTest(startApplication = false) // <1>
class MessagesLocalizedSpec extends Specification {

    private static final List<String> SUFFIXES = ["es"] // <2>

    @Inject
    ResourceLoader resourceLoader // <3>

    void "keys are localized in #suffix"(String suffix) {
        expect:
        keys("classpath:i18n/messages_en.properties") ==
                keys("classpath:i18n/messages_${suffix}.properties") // <4>

        where:
        suffix << SUFFIXES
    }

    private Set<String> keys(String path) {
        Optional<InputStream> inputStreamOptional = resourceLoader.getResourceAsStream(path)
        assert inputStreamOptional.present
        inputStreamOptional.get().withCloseable { InputStream inputStream ->
            Properties properties = new Properties()
            properties.load(inputStream)
            properties.keySet()*.toString() as Set<String>
        }
    }
}
