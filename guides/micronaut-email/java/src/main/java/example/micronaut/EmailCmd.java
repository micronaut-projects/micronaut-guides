package example.micronaut;

import io.micronaut.core.annotation.Introspected;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

//tag::clazzwithannotations[]
@EmailConstraints
//end::clazzwithannotations[]
//tag::clazz[]
@Introspected
public class EmailCmd implements Email {
//end::clazz[]

    //tag::properties[]
    @NotNull
    @NotBlank
    private String recipient;

    @NotNull
    @NotBlank
    private String subject;

    private List<String> cc = new ArrayList<>();
    private List<String> bcc = new ArrayList<>();

    private String htmlBody;
    private String textBody;
    private String replyTo;
    //end::properties[]

    //tag::settersandgetters[]
    @Override
    public String getRecipient() {
        return recipient;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    @Override
    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    @Override
    public List<String> getCc() {
        return cc;
    }

    public void setCc(List<String> cc) {
        this.cc = cc;
    }

    @Override
    public List<String> getBcc() {
        return bcc;
    }

    public void setBcc(List<String> bcc) {
        this.bcc = bcc;
    }

    @Override
    public String getHtmlBody() {
        return htmlBody;
    }

    public void setHtmlBody(String htmlBody) {
        this.htmlBody = htmlBody;
    }

    @Override
    public String getTextBody() {
        return textBody;
    }

    public void setTextBody(String textBody) {
        this.textBody = textBody;
    }

    @Override
    public String getReplyTo() {
        return replyTo;
    }

    public void setReplyTo(String replyTo) {
        this.replyTo = replyTo;
    }
}
//end::settersandgetters[]
/*
//tag::close[]
    // getters & setters
}
//end::close[]
 */