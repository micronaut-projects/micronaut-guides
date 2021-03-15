package example.micronaut;

import io.micronaut.context.ApplicationContext
import spock.lang.Specification
import spock.util.environment.RestoreSystemProperties

class AwsSesMailServiceSpec extends Specification {

    void "aws ses mail service is not loaded if system property is not present"() {
        given:
        ApplicationContext ctx = ApplicationContext.run()

        expect:
        !ctx.containsBean(AwsSesMailService.class)

        cleanup:
        ctx.close();
    }

    @RestoreSystemProperties
    void "aws ses mail service is loaded if system properties are present"() {
        given:
        System.setProperty("aws.accessKeyId", "XXXX")
        System.setProperty("aws.secretAccessKey", "YKYY")
        System.setProperty("aws.region", "XXXX")
        System.setProperty("aws.sourceemail", "me@micronaut.example")
        ApplicationContext ctx = ApplicationContext.run()

        when:
        AwsSesMailService bean = ctx.getBean(AwsSesMailService.class)

        then:
        "me@micronaut.example" == bean.sourceEmail

        cleanup:
        ctx.close()
    }
}
