package example.micronaut

import io.micronaut.context.MessageSource
import io.micronaut.context.annotation.Factory
import io.micronaut.context.i18n.ResourceBundleMessageSource
import jakarta.inject.Singleton

@Factory // <1>
class MessageSourceFactory {

    @Singleton // <2>
    fun createMessageSource(): MessageSource =
        ResourceBundleMessageSource("i18n.messages")
}
