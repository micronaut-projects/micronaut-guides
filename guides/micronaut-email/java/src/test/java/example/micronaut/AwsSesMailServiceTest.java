package example.micronaut;

import io.micronaut.context.ApplicationContext;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import org.junit.jupiter.api.condition.DisabledIfEnvironmentVariable;

@DisabledIfEnvironmentVariable(named = "AWS_SECRET_ACCESS_KEY", matches = ".*")
class AwsSesMailServiceTest {

    @Test
    public void awsSesMailServiceIsNotLoadedIfSystemPropertyIsNotPresent() {
        ApplicationContext ctx = ApplicationContext.run();
        assertFalse(ctx.containsBean(AwsSesMailService.class));
        ctx.close();
    }

    @Test
    public void awsSesMailServiceIsLoadedIfSystemPropertiesArePresent() {
        String accesskeyid = System.getProperty("aws.accessKeyId");
        String awssecretkey = System.getProperty("aws.secretAccessKey");
        String awsregion = System.getProperty("aws.region");
        String sourceemail = System.getProperty("aws.sourceemail");

        System.setProperty("aws.accessKeyId", "XXXX");
        System.setProperty("aws.secretAccessKey", "YKYY");
        System.setProperty("aws.region", "XXXX");
        System.setProperty("aws.sourceemail", "me@micronaut.example");

        ApplicationContext ctx = ApplicationContext.run();
        AwsSesMailService bean = ctx.getBean(AwsSesMailService.class);
        assertEquals("me@micronaut.example", bean.sourceEmail);
        ctx.close();

        if (awsregion == null) {
            System.clearProperty("aws.region");
        } else {
            System.setProperty("aws.region", awsregion);
        }
        if (sourceemail == null) {
            System.clearProperty("aws.sourceemail");
        } else {
            System.setProperty("aws.sourceemail", sourceemail);
        }
        if (accesskeyid == null) {
            System.clearProperty("aws.accessKeyId");
        } else {
            System.setProperty("aws.accessKeyId", accesskeyid);
        }
        if (awssecretkey == null) {
            System.clearProperty("aws.secretAccessKey");
        } else {
            System.setProperty("aws.secretAccessKey", awssecretkey);
        }
    }
}
