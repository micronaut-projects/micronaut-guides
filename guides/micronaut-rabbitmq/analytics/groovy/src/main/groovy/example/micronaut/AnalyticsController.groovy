package example.micronaut

import groovy.transform.CompileStatic
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get

@CompileStatic
@Controller('/analytics')
class AnalyticsController {

    private final AnalyticsService analyticsService

    AnalyticsController(AnalyticsService analyticsService) {
        this.analyticsService = analyticsService
    }

    @Get('/') // <1>
    List<BookAnalytics> listAnalytics() {
        return analyticsService.listAnalytics()
    }
}
