package example.micronaut

import io.micronaut.sourcegen.annotations.Builder

@Builder
data class Widget(val name: String, val id: Int)