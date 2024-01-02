package example.micronaut;
/*
//tag::package[]
package example.micronaut;
//end::package[]
*/
//tag::clazz[]

import io.micronaut.core.annotation.Nullable;
import io.micronaut.http.HttpHeaders;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Header;
import io.micronaut.http.annotation.QueryValue;
import io.micronaut.http.client.annotation.Client;
import org.reactivestreams.Publisher;

import jakarta.validation.constraints.Pattern;
import java.util.List;

@Header(name = "User-Agent", value = "https://micronautguides.com")
@Header(name = "Accept", value = "application/vnd.github.v3+json, application/json") // <1>
@Client(id = "githubv3") // <2>
public interface GithubApiClient {

    @Get("/user") // <3>
    Publisher<GithubUser> getUser( // <4>
            @Header(HttpHeaders.AUTHORIZATION) String authorization); // <5>
    //end::clazz[]
    //tag::repos[]
    @Get("/user/repos{?sort,direction}") // <1>
    List<GithubRepo> repos(
            @Pattern(regexp = "created|updated|pushed|full_name") @Nullable @QueryValue String sort, // <2>
            @Pattern(regexp = "asc|desc") @Nullable @QueryValue String direction, // <2>
            @Header(HttpHeaders.AUTHORIZATION) String authorization);
    //end::repos[]
//tag::clazz[]
}
//end::clazz[]
