package example.micronaut

import io.micronaut.context.annotation.Factory
import io.micronaut.core.annotation.AnnotationValue
import io.micronaut.core.util.StringUtils
import io.micronaut.validation.validator.constraints.ConstraintValidator
import io.micronaut.validation.validator.constraints.ConstraintValidatorContext
import jakarta.inject.Singleton

@Factory
class EmailConstraintsFactory {

    @Singleton
    fun emailBodyValidator(): ConstraintValidator<EmailConstraints, EmailCmd> {
        return ConstraintValidator<EmailConstraints, EmailCmd> { value: EmailCmd?, annotationMetadata: AnnotationValue<EmailConstraints?>?, context: ConstraintValidatorContext? ->
            value != null && (StringUtils.isNotEmpty(value.textBody) || StringUtils.isNotEmpty(value.htmlBody))
        }
    }
}
