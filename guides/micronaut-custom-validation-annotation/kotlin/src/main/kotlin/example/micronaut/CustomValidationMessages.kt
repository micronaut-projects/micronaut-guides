package example.micronaut

import io.micronaut.context.StaticMessageSource
import jakarta.inject.Singleton

@Singleton
class CustomValidationMessages() : StaticMessageSource() {
    init {
        addMessage(E164::class.java.getName() + MESSAGE_SUFFIX, E164_MESSAGE)
    }

    companion object {
        private const val E164_MESSAGE = "must be a phone in E.164 format"
        private const val MESSAGE_SUFFIX = ".message"
    }

}