package example.micronaut.domain;

import java.time.LocalDateTime;
import java.util.List;

public record Post(
        Long id,
        String title,
        String content,
        User createdBy,
        List<Comment> comments,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {}
