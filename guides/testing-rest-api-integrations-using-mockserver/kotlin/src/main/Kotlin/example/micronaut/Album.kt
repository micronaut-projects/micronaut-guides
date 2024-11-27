package example.micronaut;

import io.micronaut.serde.annotation.Serdeable
import java.util.List

@Serdeable // <1>


data class Album(val albumId: Long, val photos: List<Photo>)

