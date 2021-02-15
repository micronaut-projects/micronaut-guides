//tag::clazz[]
package example.micronaut;

import io.micronaut.http.HttpHeaders;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Header;
import io.micronaut.http.annotation.QueryValue;
import io.micronaut.http.client.annotation.Client;
import io.reactivex.Flowable;

import javax.annotation.Nullable;
import javax.validation.constraints.Pattern;
import java.util.List;

@Header(name = "User-Agent", value = "https://micronautguides.com")
@Header(name = "Accept", value = "application/vnd.github.v3+json, application/json") // <1>
@Client(id = "githubv3")
public interface GithubApiClient {

    @Get("/user") // <3>
    Flowable<GithubUser> getUser( // <4>
            @Header(HttpHeaders.AUTHORIZATION) String authorization); // <5>
    //end::clazz[]
    //tag::repos[]
    @Get("/user/repos{?sort,direction}") // <1>
    List<GithubRepo> repos(
            @Pattern(regexp = "created|updated|pushed|full_name") @Nullable @QueryValue String sort, // <2>
            @Pattern(regexp = "asc|desc") @Nullable @QueryValue String direction, // <2>
            @Header(HttpHeaders.AUTHORIZATION) String authorization);
    //end::repos[]
}
