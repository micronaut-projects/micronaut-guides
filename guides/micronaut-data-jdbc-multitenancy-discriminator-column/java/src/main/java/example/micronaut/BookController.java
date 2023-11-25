package example.micronaut;

import io.micronaut.data.runtime.multitenancy.TenantResolver;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.scheduling.TaskExecutors;
import io.micronaut.scheduling.annotation.ExecuteOn;
import java.util.List;

@Controller("/books") // <1>
class BookController {
    private final TenantResolver tenantResolver;
    private final BookRepository bookRepository;

    BookController(TenantResolver tenantResolver, BookRepository bookRepository) { // <2>
        this.tenantResolver = tenantResolver;
        this.bookRepository = bookRepository;
    }

    @ExecuteOn(TaskExecutors.BLOCKING) // <3>
    @Get // <4>
    List<Book> index() {
        return bookRepository.findAllByTenant(tenantResolver.resolveTenantIdentifier().toString());
    }
}
