package example.micronaut.domain;

import static example.micronaut.jooq.Tables.COMMENTS;
import static example.micronaut.jooq.tables.Posts.POSTS;
import static org.jooq.Records.mapping;
import static org.jooq.impl.DSL.multiset;
import static org.jooq.impl.DSL.row;
import static org.jooq.impl.DSL.select;

import java.util.Optional;

import jakarta.inject.Singleton;
import org.jooq.DSLContext;

@Singleton // <1>
class PostRepository {

    private final DSLContext dsl;

    PostRepository(DSLContext dsl) {
        this.dsl = dsl;
    }

    public Optional<Post> getPostById(Long id) {
        return this.dsl
                .select(
                        POSTS.ID,
                        POSTS.TITLE,
                        POSTS.CONTENT,
                        row(POSTS.users().ID, POSTS.users().NAME, POSTS.users().EMAIL)
                                .mapping(User::new)
                                .as("createdBy"),
                        multiset(
                                select(
                                        COMMENTS.ID,
                                        COMMENTS.NAME,
                                        COMMENTS.CONTENT,
                                        COMMENTS.CREATED_AT,
                                        COMMENTS.UPDATED_AT
                                )
                                        .from(COMMENTS)
                                        .where(POSTS.ID.eq(COMMENTS.POST_ID))
                        )
                                .as("comments")
                                .convertFrom(r -> r.map(mapping(Comment::new))),
                        POSTS.CREATED_AT,
                        POSTS.UPDATED_AT)
                .from(POSTS)
                .where(POSTS.ID.eq(id))
                .fetchOptional(mapping(Post::new));
    }
}
