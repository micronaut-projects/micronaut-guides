package example.micronaut

import io.micronaut.core.annotation.Introspected
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

//tag::clazzwithannotations[]
@EmailConstraints
//end::clazzwithannotations[]
//tag::clazz[]
@Introspected
class EmailCmd : Email {
//end::clazz[]

    //tag::properties[]
    @NotBlank
    @NotNull
    var recipient: String? = null

    @NotBlank
    @NotNull
    var subject: String? = null

    var cc: List<String>? = null

    var bcc: List<String>? = null

    var htmlBody: String? = null

    var textBody: String? = null

    var replyTo: String? = null
    //end::properties[]

    //tag::settersandgetters[]
    override fun recipient(): String? {
        return this.recipient
    }

    override fun cc(): List<String>? {
        return this.cc
    }

    override fun bcc(): List<String>? {
        return this.bcc
    }

    override fun subject(): String? {
        return this.subject
    }

    override fun htmlBody(): String? {
        return this.htmlBody
    }

    override fun textBody(): String? {
        return this.textBody
    }

    override fun replyTo(): String? {
        return this.replyTo
    }
}
//end::settersandgetters[]
/*
//tag::close[]
    // getters & setters
}
//end::close[]
 */

