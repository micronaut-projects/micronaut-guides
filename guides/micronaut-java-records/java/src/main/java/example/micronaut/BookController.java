package example.micronaut;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import io.micronaut.scheduling.TaskExecutors
import io.micronaut.scheduling.annotation.ExecuteOn

@Controller("/books") // <1>
class BookController {

    private final BookRepository bookRepository;
    private final ValueAddedTaxConfiguration valueAddedTaxConfiguration;

    BookController(BookRepository bookRepository, // <2>
                   ValueAddedTaxConfiguration valueAddedTaxConfiguration) {
        this.bookRepository = bookRepository;
        this.valueAddedTaxConfiguration = valueAddedTaxConfiguration;
    }

    @ExecuteOn(TaskExecutors.IO) // <3>
    @Get // <4>
    List<BookForSale> index() { // <5>
        return bookRepository.find()
                .stream()
                .map(bookCard -> new BookForSale(bookCard.isbn(), 
                    bookCard.title(), 
                    salePrice(bookCard)))
                .collect(Collectors.toList());
    }

    @NonNull
    private BigDecimal salePrice(@NonNull BookCard bookCard) {
        return bookCard.price()
                .add(bookCard.price()
                    .multiply(valueAddedTaxConfiguration.percentage()
                        .divide(new BigDecimal("100.0"), 2, RoundingMode.HALF_DOWN)))
                .setScale(2, RoundingMode.HALF_DOWN);
    }
}
