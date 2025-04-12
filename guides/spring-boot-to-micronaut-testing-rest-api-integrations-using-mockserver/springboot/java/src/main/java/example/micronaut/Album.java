package example.micronaut;

import java.util.List;

public record Album(Long albumId, List<Photo> photos) {
}
