package example.micronaut;

import io.micronaut.serde.annotation.Serdeable;

@Serdeable
public class GithubRelease {

    private String name;
    private String url;

    public GithubRelease() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
