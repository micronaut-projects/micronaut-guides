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
package example.micronaut

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.micronaut.context.annotation.ConfigurationBuilder
import io.micronaut.context.annotation.ConfigurationProperties
import io.micronaut.serde.annotation.Serdeable

@Serdeable // <1>
@JsonIgnoreProperties("builder") // <2>
//tag::teamConfigClassNoBuilder[]
@ConfigurationProperties("team")
class TeamConfiguration  {
    var name: String? = null
    var color: String? = null
    var playerNames: List<String>? = null
//end::teamConfigClassNoBuilder[]
//tag::teamConfigClassBuilder[]
    @ConfigurationBuilder(prefixes = ["with"], configurationPrefix = "team-admin") // <3>
    var builder = TeamAdmin.Builder() // <4>
//end::teamConfigClassBuilder[]
}

//tag::gettersandsetters[]
//end::gettersandsetters[]