package example.micronaut

import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class AwsResourceAccessConditionTest {

    @Test
    fun conditionIsTrueIfSystemPropertiesArePresent() {
        val accesskeyid = System.getProperty("aws.accessKeyId")
        val awssecretkey = System.getProperty("aws.secretAccessKey")

        System.setProperty("aws.accessKeyId", "XXXX")
        System.setProperty("aws.secretAccessKey", "YYYY")
        val condition = AwsResourceAccessCondition()
        assertTrue(condition.matches(null))

        if (accesskeyid == null) {
            System.clearProperty("aws.accessKeyId")
        } else {
            System.setProperty("aws.accessKeyId", accesskeyid)
        }
        if (awssecretkey == null) {
            System.clearProperty("aws.secretAccessKey")
        } else {
            System.setProperty("aws.secretAccessKey", awssecretkey)
        }
    }

    @Test
    fun conditionIsFalseIfSystemPropertiesAreNotPresent() {
        val condition = AwsResourceAccessCondition()
        assertFalse(condition.matches(null))
    }
}
