package example.micronaut

import groovy.transform.CompileStatic
import io.micronaut.context.annotation.Factory
import io.micronaut.core.annotation.AnnotationValue
import io.micronaut.core.annotation.NonNull
import io.micronaut.core.annotation.Nullable
import io.micronaut.core.util.StringUtils
import io.micronaut.validation.validator.constraints.ConstraintValidator
import io.micronaut.validation.validator.constraints.ConstraintValidatorContext

import jakarta.inject.Singleton

@Factory
@CompileStatic
class EmailConstraintsFactory {

    @Singleton
    ConstraintValidator<EmailConstraints, EmailCmd> emailBodyValidator() {
        return new ConstraintValidator<EmailConstraints, EmailCmd>() {
            @Override
            boolean isValid(@Nullable EmailCmd value,
                            @NonNull AnnotationValue<EmailConstraints> annotationMetadata,
                            @NonNull ConstraintValidatorContext context) {
                StringUtils.isNotEmpty(value?.textBody) || StringUtils.isNotEmpty(value?.htmlBody)
            }
        }
    }
}
