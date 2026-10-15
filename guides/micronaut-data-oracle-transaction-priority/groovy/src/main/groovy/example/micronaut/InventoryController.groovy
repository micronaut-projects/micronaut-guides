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

import groovy.transform.CompileStatic
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.Post
import io.micronaut.http.annotation.QueryValue
import io.micronaut.scheduling.TaskExecutors
import io.micronaut.scheduling.annotation.ExecuteOn
import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min

import java.time.Duration

@CompileStatic
@ExecuteOn(TaskExecutors.BLOCKING) // <1>
@Controller('/inventory')
class InventoryController {

    private final InventoryService inventoryService

    InventoryController(InventoryService inventoryService) {
        this.inventoryService = inventoryService
    }

    @Get
    InventoryItem item() {
        inventoryService.find()
    }

    @Post('/reset')
    InventoryItem reset() {
        inventoryService.reset()
    }

    @Post('/reconcile')
    InventoryItem reconcile(@QueryValue(defaultValue = '20') @Min(1L) @Max(120L) int countSeconds) { // <2>
        inventoryService.reconcile(Duration.ofSeconds(countSeconds))
    }

    @Post('/checkout')
    InventoryItem checkout() {
        inventoryService.checkout()
    }
}
