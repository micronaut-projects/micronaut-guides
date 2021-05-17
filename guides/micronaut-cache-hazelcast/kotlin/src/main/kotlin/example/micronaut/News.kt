package example.micronaut

import io.micronaut.core.annotation.Introspected
import java.time.Month

@Introspected
data class News(val month: Month, val headlines: List<String>)