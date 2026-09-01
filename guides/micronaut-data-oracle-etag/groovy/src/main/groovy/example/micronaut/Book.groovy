/*
 * Copyright 2017-2026 original authors
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
package example.micronaut

import groovy.transform.CompileStatic
import io.micronaut.data.annotation.Embeddable
import io.micronaut.data.annotation.GeneratedValue
import io.micronaut.data.annotation.Id
import io.micronaut.data.annotation.MappedEntity
import io.micronaut.data.annotation.Relation
import io.micronaut.data.annotation.sql.ETagValue
import io.micronaut.data.annotation.sql.ETaggable
import io.micronaut.data.annotation.sql.GeneratedETag

// tag::generated-etag[]
@CompileStatic
@MappedEntity
@ETaggable // <1>
class Book {
    @Id
    @GeneratedValue
    Long id
    String title
    @Relation(Relation.Kind.EMBEDDED)
    BookDetails details
    @GeneratedETag
    String etag // <2>

    Book() {
    }

    Book(Long id, String title, BookDetails details, String etag) {
        this.id = id
        this.title = title
        this.details = details
        this.etag = etag
    }

    @CompileStatic
    @Embeddable
    static class BookDetails {
        int pages
        @ETagValue(exclude = true) // <3>
        int chapters

        BookDetails() {
        }

        BookDetails(int pages, int chapters) {
            this.pages = pages
            this.chapters = chapters
        }
    }
}
// end::generated-etag[]
