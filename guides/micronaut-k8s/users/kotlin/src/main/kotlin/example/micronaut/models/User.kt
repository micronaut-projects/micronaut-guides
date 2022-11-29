package example.micronaut.models

import com.fasterxml.jackson.annotation.JsonProperty
import io.micronaut.core.annotation.Nullable
import io.micronaut.serde.annotation.Serdeable
import javax.validation.constraints.Max
import javax.validation.constraints.NotBlank

@Serdeable
data class User(@Nullable @Max(10000) val id: Int, // <1>
                @NotBlank @JsonProperty("first_name") val firstName:String,
                @NotBlank @JsonProperty("last_name")  val lastName:String,
                val username:String)
