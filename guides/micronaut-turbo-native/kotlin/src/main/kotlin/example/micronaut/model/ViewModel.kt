package example.micronaut.model

import io.micronaut.serde.annotation.Serdeable
import java.security.Principal
import java.time.ZoneOffset
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

@Serdeable
data class ViewModel(val pageTitle: String, val pageClass: String = "", val action: String? = null, val principal: Principal? = null) {
    val renderDate: String get() = ZonedDateTime.now(ZoneOffset.UTC).format(DateTimeFormatter.RFC_1123_DATE_TIME)
}