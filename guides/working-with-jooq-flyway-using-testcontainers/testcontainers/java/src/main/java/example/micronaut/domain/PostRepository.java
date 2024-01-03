/*
 * Copyright 2017-2024 original authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
