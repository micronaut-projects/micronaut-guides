package example.micronaut

import groovy.transform.CompileStatic
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get

import java.time.Month

@CompileStatic
@Controller
class NewsController {

    private final NewsService newsService

    NewsController(NewsService newsService) {
        this.newsService = newsService
    }

    @Get('/{month}')
    News index(Month month) {
        new News(month: month, headlines: newsService.headlines(month))
    }
}
