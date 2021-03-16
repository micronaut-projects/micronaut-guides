package example.micronaut

import io.micronaut.context.ApplicationContext
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class AwsSesMailServiceTest {

    @Test
    fun awsSesMailServiceIsNotLoadedIfSystemPropertyIsNotPresent() {
        val ctx = ApplicationContext.run()
        Assertions.assertFalse(ctx.containsBean(AwsSesMailService::class.java))
        ctx.close()
    }

    @Test
    fun awsSesMailServiceIsLoadedIfSystemPropertiesArePresent() {
        val accesskeyid = System.getProperty("aws.accessKeyId")
        val awssecretkey = System.getProperty("aws.secretAccessKey")
        val awsregion = System.getProperty("aws.region")
        val sourceemail = System.getProperty("aws.sourceemail")

        System.setProperty("aws.accessKeyId", "XXXX")
        System.setProperty("aws.secretAccessKey", "YKYY")
        System.setProperty("aws.region", "XXXX")
        System.setProperty("aws.sourceemail", "me@micronaut.example")

        val ctx = ApplicationContext.run()
        val bean = ctx.getBean(AwsSesMailService::class.java)
        Assertions.assertEquals("me@micronaut.example", bean.sourceEmail)

        ctx.close()

        if (awsregion == null) {
            System.clearProperty("aws.region")
        } else {
            System.setProperty("aws.region", awsregion)
        }
        if (sourceemail == null) {
            System.clearProperty("aws.sourceemail")
        } else {
            System.setProperty("aws.sourceemail", sourceemail)
        }
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
}