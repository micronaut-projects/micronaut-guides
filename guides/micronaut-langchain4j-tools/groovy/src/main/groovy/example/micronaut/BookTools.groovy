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

import dev.langchain4j.agent.tool.P
import dev.langchain4j.agent.tool.Tool
import jakarta.inject.Singleton

@Singleton // <1>
class BookTools {

    private static final Map<String, Integer> AVAILABLE_COPIES = [ // <2>
            dune       : 3,
            foundation : 1,
            neuromancer: 0]

    @Tool("Returns the number of copies of a book that the library can lend right now") // <3>
    int availableCopies(@P(name = "title", value = "The title of the book") String title) { // <4>
        AVAILABLE_COPIES.getOrDefault(title.strip().toLowerCase(Locale.ROOT), 0)
    }
}
