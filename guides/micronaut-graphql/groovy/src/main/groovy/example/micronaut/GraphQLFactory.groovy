package example.micronaut

import graphql.GraphQL
import graphql.GraphQL.Builder
import graphql.schema.GraphQLSchema
import graphql.schema.idl.RuntimeWiring
import graphql.schema.idl.SchemaGenerator
import graphql.schema.idl.SchemaParser
import graphql.schema.idl.TypeDefinitionRegistry
import io.micronaut.context.annotation.Bean
import io.micronaut.context.annotation.Factory
import io.micronaut.core.io.ResourceResolver
import org.slf4j.Logger
import org.slf4j.LoggerFactory

import jakarta.inject.Singleton

import static graphql.schema.idl.TypeRuntimeWiring.newTypeWiring

@Factory // <1>
class GraphQLFactory {

    private static final Logger LOG = LoggerFactory.getLogger(GraphQLFactory)

    @Bean
    @Singleton
    GraphQL graphQL(ResourceResolver resourceResolver, GraphQLDataFetchers graphQLDataFetchers) { // <2>
        SchemaParser schemaParser = new SchemaParser()
        SchemaGenerator schemaGenerator = new SchemaGenerator()

        // Parse the schema
        TypeDefinitionRegistry typeRegistry = new TypeDefinitionRegistry()
        Optional<InputStream> graphqlSchema = resourceResolver.getResourceAsStream("classpath:schema.graphqls") // <3>

        if (graphqlSchema.isPresent()) {
            typeRegistry.merge(schemaParser.parse(new BufferedReader(new InputStreamReader(graphqlSchema.get()))))  // <4>

            // Create the runtime wiring
            RuntimeWiring runtimeWiring = RuntimeWiring.newRuntimeWiring() // <5>
                .type(newTypeWiring("Query")
                          .dataFetcher("bookById", graphQLDataFetchers.bookByIdDataFetcher)) // <6>
                .type(newTypeWiring("Book")
                          .dataFetcher("author", graphQLDataFetchers.authorDataFetcher)) // <7>
                .build()

            // Create the executable schema
            GraphQLSchema graphQLSchema = schemaGenerator.makeExecutableSchema(typeRegistry, runtimeWiring) // <8>

            // Return the GraphQL bean
            return GraphQL.newGraphQL(graphQLSchema).build() // <9>

        } else {
            LOG.debug("No GraphQL services found, returning empty schema")
            return new Builder(GraphQLSchema.newSchema().build()).build()
        }
    }
}
