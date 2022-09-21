package example.micronaut;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;

@RestController // <1>
public class BookController {
    @Autowired // <2>
    private BookRepository bookRepository;

    @GetMapping("/books") // <3>
    Iterable<Book> list() {
        return bookRepository.findAll();
    }
}
