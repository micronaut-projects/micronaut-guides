package example.micronaut

import io.micronaut.context.annotation.Requires
import io.micronaut.context.env.Environment
import javax.inject.Singleton

@Singleton
@Requires(env = Environment.TEST)
class MockBookFetcher implements BookFetcher {

    @Override
    List<Book> fetchBooks() {
        [
            "The Empty Hearse",
            "The Hounds of Baskerville",
            "The Woman",
            "The Six Thatchers",
            "The Aluminium Crutch",
            "The Speckled Blonde",
            "The Geek Interpreter",
            "The Great Game",
            "The Blind Banker",
            "A Study in Pink"].collect { new Book(title: it) }
    }
}
