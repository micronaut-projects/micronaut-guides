package example.micronaut.models

import com.fasterxml.jackson.annotation.JsonProperty
import io.micronaut.core.annotation.Nullable
import io.micronaut.serde.annotation.Serdeable
import jakarta.validation.constraints.Max
import jakarta.validation.constraints.NotBlank

@Serdeable // <1>
data class User(@Nullable @Max(10000) val id: Int, // <2>
                @NotBlank @JsonProperty("first_name") val firstName:String,
                @NotBlank @JsonProperty("last_name")  val lastName:String,
                val username:String)
