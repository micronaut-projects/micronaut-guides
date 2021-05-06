package example.micronaut

import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.views.View

import groovy.transform.CompileStatic

@CompileStatic
@Controller // <1>
class HomeController {

    private final BookFetcher bookFetcher

    HomeController(BookFetcher bookFetcher) { // <2>
        this.bookFetcher = bookFetcher
    }

    @View("home") // <3>
    @Get // <4>
    HttpResponse<Map<String, Object>> index() {
        HttpResponse.ok([
                pagetitle: "Home",
                books: bookFetcher.fetchBooks().collect { it.title }
        ] as Map<String, Object>) // <5>
    }

}
