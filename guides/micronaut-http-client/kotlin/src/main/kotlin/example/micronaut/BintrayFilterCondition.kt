package example.micronaut

import io.micronaut.context.condition.Condition
import io.micronaut.context.condition.ConditionContext
import io.micronaut.context.exceptions.NoSuchBeanException

class BintrayFilterCondition : Condition {

    override fun matches(context: ConditionContext<*>?): Boolean {
        if (context != null && context.beanContext != null) {
            try {
                val bintrayConfiguration: BintrayConfiguration = context.beanContext.getBean(BintrayConfiguration::class.java)
                if (bintrayConfiguration.token != null && bintrayConfiguration.username != null) {
                    return true
                }
            } catch (e: NoSuchBeanException) {
            }
        }
        return false
    }

}
