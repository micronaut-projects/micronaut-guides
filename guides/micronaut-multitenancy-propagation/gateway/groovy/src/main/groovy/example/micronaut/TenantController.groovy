package example.micronaut

import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpStatus
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.cookie.Cookie
import io.micronaut.multitenancy.tenantresolver.CookieTenantResolverConfiguration
import io.micronaut.views.View


@Controller("/tenant") // <1>
class TenantController {
    private final CookieTenantResolverConfiguration cookieTenantResolverConfiguration

    TenantController(CookieTenantResolverConfiguration cookieTenantResolverConfiguration) { // <2>
        this.cookieTenantResolverConfiguration = cookieTenantResolverConfiguration
    }

    @View("tenant") // <3>
    @Get // <4>
    HttpResponse<Map<String, Object>> index() {
        Map<String, Object> model = new HashMap<>()
        model["pagetitle"] = "Tenants"
        model["tenants"] = ["sherlock", "watson"]
        HttpResponse.ok(model)
    }

    @Get("/{tenant}") // <5>
    HttpResponse tenant(String tenant) {
        final String path = "/"
        final String cookieName = cookieTenantResolverConfiguration.getCookiename()
        return HttpResponse.status(HttpStatus.FOUND).headers((headers) ->
                headers.location(URI.create(path))
        ).cookie(Cookie.of(cookieName, tenant).path(path)) // <6>
    }
}
