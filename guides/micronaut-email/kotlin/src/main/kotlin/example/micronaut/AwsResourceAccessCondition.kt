package example.micronaut

import io.micronaut.context.condition.Condition
import io.micronaut.context.condition.ConditionContext
import io.micronaut.context.env.Environment
import io.micronaut.core.util.StringUtils

class AwsResourceAccessCondition : Condition {

    override fun matches(context: ConditionContext<*>?): Boolean {
        if (StringUtils.isNotEmpty(System.getProperty("aws.accessKeyId")) && StringUtils.isNotEmpty(System.getProperty("aws.secretAccessKey"))) { // <1>
            return true
        }
        if (StringUtils.isNotEmpty(System.getenv("AWS_ACCESS_KEY_ID")) && StringUtils.isNotEmpty(System.getenv("AWS_SECRET_ACCESS_KEY"))) { // <2>
            return true
        }
        if (StringUtils.isNotEmpty(System.getenv("AWS_CONTAINER_CREDENTIALS_RELATIVE_URI"))) { // <3>
            true
        }
        return context != null && context.getBean(Environment::class.java).activeNames.contains(Environment.AMAZON_EC2) // <4>
    }

}
