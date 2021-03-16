package example.micronaut

import spock.lang.Specification
import spock.util.environment.RestoreSystemProperties

class SendGridEmailConditionSpec extends Specification {

    @RestoreSystemProperties
    void "condition is true if system properites are present"() {
        given:
        System.setProperty("sendgrid.apikey", "XXXX")
        System.setProperty("sendgrid.fromemail", "me@micronaut.example")
        SendGridEmailCondition condition = new SendGridEmailCondition()

        expect:
        condition.matches(null)
    }

    void "condition is false if system properties are not present"() {
        given:
        SendGridEmailCondition condition = new SendGridEmailCondition()

        expect:
        !condition.matches(null)
    }
}
