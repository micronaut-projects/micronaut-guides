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

import groovy.transform.CompileStatic
import jakarta.inject.Singleton

import jakarta.validation.ConstraintViolation
import jakarta.validation.Path

@CompileStatic
@Singleton
class MessageSource {

    List<String> violationsMessages(Set<ConstraintViolation<?>> violations) {
        violations.collect {violationMessage(it) }
    }

    private String violationMessage(ConstraintViolation violation) {
        StringBuilder sb = new StringBuilder()
        Path.Node lastNode = lastNode(violation.propertyPath)
        if (lastNode) {
            sb << lastNode.name << ' '
        }
        sb << violation.message
        sb
    }

    private static Path.Node lastNode(Path path) {
        Path.Node lastNode = null
        for (final Path.Node node : path) {
            lastNode = node
        }
        return lastNode
    }
}
