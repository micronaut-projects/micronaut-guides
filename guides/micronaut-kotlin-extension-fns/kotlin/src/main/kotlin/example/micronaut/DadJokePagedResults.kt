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

import io.micronaut.serde.annotation.Serdeable

@Serdeable // <1>
data class DadJokePagedResults(
    val current_page: Int,
    val limit: Int,
    val previous_page: Int,
    val next_page: Int,
    val total_jokes: Int,
    val total_pages: Int,
    val results: List<DadJoke>
)
