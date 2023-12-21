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

import io.micronaut.context.annotation.Requires
import io.micronaut.http.annotation.Consumes
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.Header
import io.micronaut.http.client.annotation.Client
import reactor.core.publisher.Mono

import static io.micronaut.context.env.Environment.TEST
import static io.micronaut.http.MediaType.TEXT_PLAIN

@Client(id = 'userecho') // <1>
@Requires(notEnv = TEST) // <2>
interface UserEchoClient extends UsernameFetcher {

    @Override
    @Consumes(TEXT_PLAIN)
    @Get('/user') // <3>
    Mono<String> findUsername(@Header('Authorization') String authorization) // <4>
}
