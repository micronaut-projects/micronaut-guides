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

import graphql.GraphQL;
import graphql.schema.GraphQLSchema;
import graphql.schema.idl.RuntimeWiring;
import graphql.schema.idl.SchemaGenerator;
import graphql.schema.idl.SchemaParser;
import graphql.schema.idl.TypeDefinitionRegistry;
import io.micronaut.context.annotation.Bean;
import io.micronaut.context.annotation.Factory;
import io.micronaut.core.io.ResourceResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.inject.Singleton;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Optional;

import static graphql.schema.idl.TypeRuntimeWiring.newTypeWiring;

@Factory // <1>
public class GraphQLFactory {

    private static final Logger LOG = LoggerFactory.getLogger(GraphQLFactory.class);

    @Bean
    @Singleton
    public GraphQL graphQL(ResourceResolver resourceResolver,
                           GraphQLDataFetchers graphQLDataFetchers) {
        SchemaParser schemaParser = new SchemaParser(); // <2>

        TypeDefinitionRegistry typeRegistry = new TypeDefinitionRegistry();
        Optional<InputStream> graphqlSchema = resourceResolver.getResourceAsStream("classpath:schema.graphqls"); // <3>

        if (graphqlSchema.isPresent()) {
            typeRegistry.merge(schemaParser.parse(new BufferedReader(new InputStreamReader(graphqlSchema.get())))); // <4>

            RuntimeWiring runtimeWiring = RuntimeWiring.newRuntimeWiring() // <5>
                    .type(newTypeWiring("Query")
                            .dataFetcher("bookById", graphQLDataFetchers.getBookByIdDataFetcher())) // <6>
                    .type(newTypeWiring("Book")
                            .dataFetcher("author", graphQLDataFetchers.getAuthorDataFetcher())) // <7>
                    .build();

            SchemaGenerator schemaGenerator = new SchemaGenerator();
            GraphQLSchema graphQLSchema = schemaGenerator.makeExecutableSchema(typeRegistry, runtimeWiring); // <8>

            return GraphQL.newGraphQL(graphQLSchema).build(); // <9>

        } else {
            LOG.debug("No GraphQL services found, returning empty schema");
            return new GraphQL.Builder(GraphQLSchema.newSchema().build()).build();
        }
    }
}
