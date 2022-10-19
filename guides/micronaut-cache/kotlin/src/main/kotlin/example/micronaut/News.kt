package example.micronaut

import io.micronaut.serde.annotation.Serdeable
import java.time.Month

@Serdeable // <1>
data class News(val month: Month, val headlines: List<String>)