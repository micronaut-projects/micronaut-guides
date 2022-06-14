package example.micronaut;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.data.repository.jpa.criteria.PredicateSpecification;

public class BookSpecifications {
    public static PredicateSpecification<BookEntity> nameLike(@NonNull String name) {
        return (root, criteriaBuilder) -> criteriaBuilder.like(root.get("name"), "%"+name+"%");
    }

    public static PredicateSpecification<BookEntity> authorLike(@NonNull String author) {
        return (root, criteriaBuilder) -> criteriaBuilder.like(root.get("author"), "%"+author+"%");
    }
}
