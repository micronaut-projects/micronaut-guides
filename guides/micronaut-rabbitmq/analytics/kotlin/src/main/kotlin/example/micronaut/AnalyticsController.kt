package example.micronaut

import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get

@Controller("/analytics")
class AnalyticsController(private val analyticsService: AnalyticsService) {

    @Get
    fun listAnalytics(): List<BookAnalytics> = // <1>
        analyticsService.listAnalytics()
}
