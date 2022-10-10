/*
 * Copyright 2017-2022 original authors
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
package io.micronaut.guides.feature.springboot.replacements;

import io.micronaut.context.annotation.Replaces;
import io.micronaut.guides.feature.springboot.SpringBootApplicationFeature;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.feature.test.Junit;
import jakarta.inject.Singleton;

@Replaces(Junit.class)
@Singleton
public class JunitReplacement extends Junit {

    @Override
    public void apply(GeneratorContext generatorContext) {
        if (!SpringBootApplicationFeature.isSpringBootApplication(generatorContext)) {
            super.apply(generatorContext);
        }
    }
}
