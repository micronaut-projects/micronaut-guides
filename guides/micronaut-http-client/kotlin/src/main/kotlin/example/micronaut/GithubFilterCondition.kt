package example.micronaut

import io.micronaut.context.condition.Condition
import io.micronaut.context.condition.ConditionContext
import io.micronaut.context.exceptions.NoSuchBeanException

class GithubFilterCondition : Condition {

    override fun matches(context: ConditionContext<*>?): Boolean {
        if (context != null && context.beanContext != null) {
            try {
                val githubConfiguration: GithubConfiguration =
                    context.beanContext.getBean(GithubConfiguration::class.java)
                if (githubConfiguration.token != null && githubConfiguration.username != null) {
                    return true
                }
            } catch (e: NoSuchBeanException) {
            }
        }
        return false
    }

}
