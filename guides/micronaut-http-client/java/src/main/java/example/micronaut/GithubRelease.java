package example.micronaut;

import io.micronaut.serde.annotation.Serdeable;

@Serdeable
public record GithubRelease(String name, String url) {
}
