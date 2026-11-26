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
package example.micronaut;

import dev.langchain4j.mcp.McpToolProvider;
import io.micronaut.langchain4j.aiservices.AiServiceCreationContext;
import io.micronaut.langchain4j.aiservices.AiServiceCustomizer;
import jakarta.inject.Singleton;

@Singleton // <1>
class AssistantCustomizer implements AiServiceCustomizer<Assistant> { // <2>

    private final McpToolProvider mcpToolProvider;

    AssistantCustomizer(McpToolProvider mcpToolProvider) { // <3>
        this.mcpToolProvider = mcpToolProvider;
    }

    @Override
    public void customize(AiServiceCreationContext<Assistant> creationContext) {
        creationContext.aiServices().toolProvider(mcpToolProvider); // <4>
    }
}
