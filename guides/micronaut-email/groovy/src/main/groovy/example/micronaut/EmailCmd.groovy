package example.micronaut

import groovy.transform.CompileStatic;
import io.micronaut.core.annotation.Introspected;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull

//tag::clazzwithannotations[]
@EmailConstraints
//end::clazzwithannotations[]
//tag::clazz[]
@Introspected
@CompileStatic
class EmailCmd implements Email {
//end::clazz[]

    //tag::properties[]
    @NotNull
    @NotBlank
    String recipient

    @NotNull
    @NotBlank
    String subject

    List<String> cc = []
    List<String> bcc = []

    String htmlBody
    String textBody
    String replyTo
    //end::properties[]
}

//tag::settersandgetters[]
//end::settersandgetters[]
//tag::close[]
//end::close[]
