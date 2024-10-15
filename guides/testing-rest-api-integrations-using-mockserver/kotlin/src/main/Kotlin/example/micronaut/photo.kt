package example.micronaut


import io.micronaut.serde.annotation.Serdeable

@Serdeable  // <1>


data class Photo(val id: Long, val title: String, val url: String, val thumbnailUrl: String
)
