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

import io.micronaut.data.annotation.Embeddable
import io.micronaut.data.annotation.GeneratedValue
import io.micronaut.data.annotation.Id
import io.micronaut.data.annotation.MappedEntity
import io.micronaut.data.annotation.Relation
import io.micronaut.data.annotation.sql.ETagValue
import io.micronaut.data.annotation.sql.ETaggable
import io.micronaut.data.annotation.sql.GeneratedETag

// tag::generated-etag[]
@MappedEntity("etag_book")
@ETaggable // <1>
data class ETagBook(
    @field:Id @field:GeneratedValue val id: Long? = null,
    val title: String,
    @field:Relation(Relation.Kind.EMBEDDED) val details: BookDetails,
    @field:GeneratedETag val etag: String? = null // <2>
) {
    @Embeddable
    data class BookDetails(
        val pages: Int,
        @field:ETagValue(exclude = true) val chapters: Int // <3>
    )
}
// end::generated-etag[]
