package example.micronaut

import io.micronaut.core.util.Toggleable
import io.micronaut.context.annotation.ConfigurationProperties
import io.micronaut.core.bind.annotation.Bindable
import io.micronaut.core.util.StringUtils

@ConfigurationProperties("app.scheduled") // <1>
interface ScheduledConfiguration : Toggleable {
    @Bindable(defaultValue = StringUtils.TRUE) // <2>
    override fun isEnabled(): Boolean
}