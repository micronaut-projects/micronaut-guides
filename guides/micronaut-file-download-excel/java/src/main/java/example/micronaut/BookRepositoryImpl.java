package example.micronaut;

import io.micronaut.core.annotation.NonNull;
import jakarta.inject.Singleton;

import java.util.Arrays;
import java.util.List;

@Singleton // <1>
public class BookRepositoryImpl implements BookRepository {

    @NonNull
    @Override
    public List<Book> findAll() {
        return Arrays.asList(
                new Book("1491950358", "Building Microservices"),
                new Book("1680502395", "Release It!"),
                new Book("0321601912", "Continuous Delivery:"));
    }
}
