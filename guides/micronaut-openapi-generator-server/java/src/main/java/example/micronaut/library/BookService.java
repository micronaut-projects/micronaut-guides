package example.micronaut.library;

import example.micronaut.library.model.BookAvailability;
import example.micronaut.library.model.BookInfo;
import jakarta.inject.Singleton;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Singleton // <1>
public class BookService {
    private static final List<BookInfo> BOOKS = new ArrayList<>(Arrays.asList(
            new BookInfo("Alice's Adventures in Wonderland", BookAvailability.AVAILABLE)
                    .author("Lewis Carroll"),
            new BookInfo("The Hitchhiker's Guide to the Galaxy", BookAvailability.RESERVED)
                    .author("Douglas Adams"),
            new BookInfo("Java Guide for Beginners", BookAvailability.AVAILABLE)
    ));

    public void addBook(BookInfo bookInfo) {
        BOOKS.add(bookInfo);
    }

    public List<BookInfo> searchForBook(String name, String author) {
        // Filter books by title and author name if parameter present
        return BOOKS.stream()
                .filter(b -> (name == null || b.getName().contains(name)) &&
                        (author == null || b.getAuthor() != null && b.getAuthor().contains(author)))
                .collect(Collectors.toList());
    }
}
