package example.micronaut

import groovy.transform.CompileStatic
import io.micronaut.context.MessageSource
import io.micronaut.context.annotation.Factory
import io.micronaut.context.i18n.ResourceBundleMessageSource
import jakarta.inject.Singleton

@CompileStatic
@Factory // <1>
class MessageSourceFactory {
    @Singleton // <2>
    MessageSource createMessageSource() {
        return new ResourceBundleMessageSource('i18n.messages')
    }
}
