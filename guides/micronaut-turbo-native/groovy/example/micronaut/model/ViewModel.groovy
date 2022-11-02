package example.micronaut.model

import io.micronaut.core.annotation.Nullable
import io.micronaut.serde.annotation.Serdeable

import java.security.Principal
import java.time.ZoneOffset
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

@Serdeable
class ViewModel {

    final String pageTitle
    final String pageClass
    @Nullable
    final String action
    @Nullable
    final Principal principal

    ViewModel(String pageTitle, String pageClass = "", @Nullable String action = null, @Nullable Principal principal = null) {
        this.pageTitle = pageTitle
        this.pageClass = pageClass
        this.action = action
        this.principal = principal
    }

    String getRenderDate() {
        return ZonedDateTime.now(ZoneOffset.UTC).format(DateTimeFormatter.RFC_1123_DATE_TIME)
    }
}