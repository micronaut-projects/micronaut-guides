package example.micronaut;

import io.micronaut.context.annotation.Factory;
import io.micronaut.core.util.StringUtils;
import io.micronaut.validation.validator.constraints.ConstraintValidator;

import javax.inject.Singleton;

@Factory
public class EmailConstraintsFactory {

    @Singleton
    ConstraintValidator<EmailConstraints, EmailCmd> emailBodyValidator() {
        return (value, annotationMetadata, context) ->
                value != null &&
                        (StringUtils.isNotEmpty(value.getTextBody()) || StringUtils.isNotEmpty(value.getHtmlBody()));
    }
}
