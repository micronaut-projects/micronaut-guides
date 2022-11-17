//tag::packageandimports[]
package example.micronaut.clients;

import example.micronaut.models.User;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.retry.annotation.Recoverable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
//end::packageandimports[]

/*
//tag::harcoded[]
@Client("http://localhost:8081") // <1>
//end::harcoded[]
*/
//tag::k8s[]
@Client("users") // <1>
//end::k8s[]
//tag::clazz[]
public interface UsersClient {
    @Get("/users/{id}")
    Mono<User> getById(Integer id);

    @Post("/users")
    Mono<User> createUser(@Body User user);

    @Get("/users")
    Flux<User> getUsers();
}
//end::clazz[]
