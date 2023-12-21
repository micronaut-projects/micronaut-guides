/*
 * Copyright 2017-2023 original authors
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

class TeamAdmin private constructor(
        val manager: String?,
        val coach: String?,
        val president: String?) { // <1>
    data class Builder( // <2>
            var manager: String? = null,
            var coach: String? = null,
            var president: String? = null) {
        fun withManager(manager: String) = apply { this.manager = manager } // <3>
        fun withCoach(coach: String) = apply { this.coach = coach }
        fun withPresident(president: String) = apply { this.president = president }
        fun build() = TeamAdmin(manager, coach, president) // <4>
    }
}