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
package example.micronaut;

import io.micronaut.context.annotation.Factory;
import io.micronaut.validation.validator.constraints.ConstraintValidator;
import jakarta.inject.Singleton;

@Factory // <1>
class CustomValidationFactory {

    /**
     * @return A {@link ConstraintValidator} implementation of a {@link E164} constraint for type {@link String}.
     */
    @Singleton // <2>
    ConstraintValidator<E164, String> e164Validator() {
        return (value, annotationMetadata, context) -> E164Utils.isValid(value);
    }
}