/*
 * Copyright 2017-2024 original authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
