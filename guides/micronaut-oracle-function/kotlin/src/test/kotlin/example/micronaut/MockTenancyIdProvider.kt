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

import io.micronaut.context.annotation.Context
import io.micronaut.context.annotation.Replaces
import io.micronaut.oraclecloud.core.TenancyIdProvider
import jakarta.inject.Singleton

@Context
@Singleton
@Replaces(value = TenancyIdProvider::class)
class MockTenancyIdProvider : TenancyIdProvider {

    override fun getTenancyId() = "id"
}
