package example.micronaut

import io.micronaut.context.condition.Condition
import io.micronaut.context.condition.ConditionContext
import io.micronaut.context.exceptions.NoSuchBeanException

class GithubFilterCondition : Condition { // <1>

    override fun matches(context: ConditionContext<*>?): Boolean {
        if (context != null && context.beanContext != null) {
            try {
                val githubConfiguration: GithubConfiguration =
                    context.beanContext.getBean(GithubConfiguration::class.java) // <2>
                if (githubConfiguration.token != null && githubConfiguration.username != null) {
                    return true // <3>
                }
            } catch (e: NoSuchBeanException) {
            }
        }
        return false
    }

}
