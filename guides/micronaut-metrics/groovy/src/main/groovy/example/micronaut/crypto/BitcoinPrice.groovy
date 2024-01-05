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
package example.micronaut.crypto

import groovy.transform.CompileStatic
import io.micronaut.serde.annotation.Serdeable

@CompileStatic
@Serdeable // <1>
class BitcoinPrice {

    private final Data data

    BitcoinPrice(Data data) {
        this.data = data
    }

    float getPrice() {
        return data.price
    }

    @CompileStatic
    @Serdeable // <1>
    static class Data {

        final float price

        Data(float price) {
            this.price = price
        }
    }
}
