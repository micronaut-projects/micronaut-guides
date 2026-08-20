/*
 * Copyright 2017-2026 original authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 */
package example.micronaut

import io.micronaut.data.annotation.GeneratedValue
import io.micronaut.data.annotation.Id
import io.micronaut.data.annotation.MappedEntity
import io.micronaut.data.annotation.sql.ETagValue
import io.micronaut.data.annotation.sql.GeneratedETag

// tag::explicit-etag[]
@MappedEntity("explicit_etag_book")
data class ExplicitETagBook(
    @field:Id @field:GeneratedValue @field:ETagValue val id: Long? = null,
    @field:ETagValue val title: String,
    val notes: String,
    @field:GeneratedETag(function = "SYS_ROW_ETAG") val etag: String? = null
)
// end::explicit-etag[]
