package example.micronaut.model;

import io.micronaut.core.annotation.Nullable;
import io.micronaut.serde.annotation.Serdeable;

import java.security.Principal;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

@Serdeable
public class ViewModel {

    private final String pageTitle;
    private final String pageClass;
    @Nullable
    private final String action;
    @Nullable
    private final Principal principal;

    public ViewModel(String pageTitle) {
        this(pageTitle, "", null, null);
    }

    public ViewModel(String pageTitle, String pageClass) {
        this(pageTitle, pageClass, null, null);
    }

    public ViewModel(String pageTitle, String pageClass, String action) {
        this(pageTitle, pageClass, action, null);
    }

    public ViewModel(String pageTitle, String pageClass, @Nullable String action, @Nullable Principal principal) {
        this.pageTitle = pageTitle;
        this.pageClass = pageClass;
        this.action = action;
        this.principal = principal;
    }

    public String getPageTitle() {
        return pageTitle;
    }

    public String getPageClass() {
        return pageClass;
    }

    @Nullable
    public String getAction() {
        return action;
    }

    @Nullable
    public Principal getPrincipal() {
        return principal;
    }

    public String getRenderDate() {
        return ZonedDateTime.now(ZoneOffset.UTC).format(DateTimeFormatter.RFC_1123_DATE_TIME);
    }
}