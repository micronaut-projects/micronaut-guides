package example.micronaut;

import edu.umd.cs.findbugs.annotations.NonNull;

import javax.inject.Singleton;
import java.util.List;

import java.util.Arrays;

@Singleton // <1>
public class BookRepositoryImpl implements BookRepository {
    @NonNull
    @Override
    public List<Book> findAll() {
        Book buildingMicroservices = new Book("1491950358", "Building Microservices");
        Book releaseIt = new Book("1680502395", "Release It!");
        Book cidelivery = new Book("0321601912", "Continuous Delivery:");
        return Arrays.asList(buildingMicroservices, releaseIt, cidelivery);
    }
}
