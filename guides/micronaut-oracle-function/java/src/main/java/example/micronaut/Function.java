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
package example.micronaut;

import io.micronaut.core.annotation.ReflectiveAccess;
import io.micronaut.oraclecloud.core.TenancyIdProvider;
import io.micronaut.oraclecloud.function.OciFunction;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import java.util.Date;

@Singleton
@ReflectiveAccess // <1>
public class Function extends OciFunction {

    @Inject
    TenancyIdProvider tenantIdProvider;

    public String handleRequest() {
        String tenancyId = tenantIdProvider.getTenancyId();
        return "Your tenancy is: " + tenancyId;
    }
}
