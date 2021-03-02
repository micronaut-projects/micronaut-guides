package example.micronaut

import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.reactivex.Flowable

@Controller("/books") // <1>
class BookController(private val bookCatalogueOperations: BookCatalogueOperations,
                     private val bookInventoryOperations: BookInventoryOperations) { // <2>

    @Get // <3>
    fun index(): Flowable<BookRecommendation> {
        return bookCatalogueOperations.findAll()
                .flatMapMaybe { b ->
                    bookInventoryOperations.stock(b.isbn)
                            .filter { hasStock -> hasStock }
                            .map { _ -> b }
                }.map { (_, name) -> BookRecommendation(name) }
    }
}
