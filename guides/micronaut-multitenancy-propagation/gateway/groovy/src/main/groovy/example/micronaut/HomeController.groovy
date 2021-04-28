package example.micronaut

import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.views.View

@Controller // <1>
class HomeController {

    private final BookFetcher bookFetcher

    HomeController(BookFetcher bookFetcher) { // <2>
        this.bookFetcher = bookFetcher
    }

    @View("home") // <3>
    @Get // <4>
    HttpResponse<Map<String, Object>> index() {
        List<Book> books = bookFetcher.fetchBooks()

        Map<String, Object> model = new HashMap<>()
        model["pagetitle"] = "Home"
        model["books"] = books.collect { it.title }
        HttpResponse.ok(model)  // <5>
    }

}
