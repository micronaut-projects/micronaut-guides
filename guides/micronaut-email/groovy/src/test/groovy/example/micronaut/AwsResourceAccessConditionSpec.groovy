package example.micronaut;

import spock.lang.IgnoreIf
import spock.lang.Specification
import spock.util.environment.RestoreSystemProperties

@IgnoreIf({ env['AWS_SECRET_ACCESS_KEY'] })
class AwsResourceAccessConditionSpec extends Specification {
    
    @RestoreSystemProperties
    void "condition is true if system properties are present"() {
        when:
        System.setProperty("aws.accessKeyId", "XXXX")
        System.setProperty("aws.secretAccessKey", "YYYY")
        AwsResourceAccessCondition condition = new AwsResourceAccessCondition()

        then:
        condition.matches(null)
    }

    void "condition is false if System Properties are not present"() {
        given:
        AwsResourceAccessCondition condition = new AwsResourceAccessCondition();

        expect:
        !condition.matches(null)
    }
}
