package example.micronaut

import io.micronaut.core.annotation.Introspected
import java.time.Month
import java.io.Serializable

@Introspected // <1>
data class News(val month: Month, val headlines: List<String>) : Serializable // <2>