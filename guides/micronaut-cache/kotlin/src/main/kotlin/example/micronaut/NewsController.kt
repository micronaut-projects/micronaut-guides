package example.micronaut

import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import java.time.Month

@Controller
class NewsController(val newsService: NewsService) {
    @Get("/{month}")
    fun index(month: Month): News {
        return News(month, newsService.headlines(month).orEmpty())
    }
}
