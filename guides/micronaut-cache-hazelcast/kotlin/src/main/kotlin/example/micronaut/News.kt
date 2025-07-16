package example.micronaut

import io.micronaut.core.annotation.Introspected
import java.time.Month

@Introspected // <1>
data class News(val month: Month, val headlines: List<String>)