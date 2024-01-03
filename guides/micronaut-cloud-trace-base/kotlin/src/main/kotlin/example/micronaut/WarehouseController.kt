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

import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.Post
import io.micronaut.scheduling.TaskExecutors
import io.micronaut.scheduling.annotation.ExecuteOn
import java.util.Random

@ExecuteOn(TaskExecutors.BLOCKING) // <1>
@Controller("/warehouse") // <2>
class WarehouseController {

    @Get("/count") // <3>
    fun getItemCount() : HttpResponse<Int> = HttpResponse.ok(Random().nextInt(11))


    @Post("/order") // <4>
    fun order() : HttpResponse<Any> {
        try {
            //To simulate an external process taking time
            Thread.sleep(500)
        } catch (e: InterruptedException) {
        }

        return HttpResponse.accepted()
    }

}