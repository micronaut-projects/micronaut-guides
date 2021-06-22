package example.micronaut;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.DisabledIfEnvironmentVariable;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisabledIfEnvironmentVariable(named = "AWS_SECRET_ACCESS_KEY", matches = ".*")
class AwsResourceAccessConditionTest {

    @Test
    public void conditionIsTrueIfSystemPropertiesArePresent() {

        String accesskeyid = System.getProperty("aws.accessKeyId");
        String awssecretkey = System.getProperty("aws.secretAccessKey");
        System.setProperty("aws.accessKeyId", "XXXX");
        System.setProperty("aws.secretAccessKey", "YYYY");

        AwsResourceAccessCondition condition = new AwsResourceAccessCondition();
        assertTrue(condition.matches(null));

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

    @Test
    public void conditionIsFalseIfSystemPropertiesAreNotPresent() {
        AwsResourceAccessCondition condition = new AwsResourceAccessCondition();
        assertFalse(condition.matches(null));
    }
}
