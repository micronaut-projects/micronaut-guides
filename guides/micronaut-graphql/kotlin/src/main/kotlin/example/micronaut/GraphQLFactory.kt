package example.micronaut

import graphql.GraphQL
import graphql.schema.GraphQLSchema
import graphql.schema.idl.RuntimeWiring
import graphql.schema.idl.SchemaGenerator
import graphql.schema.idl.SchemaParser
import graphql.schema.idl.TypeDefinitionRegistry
import graphql.schema.idl.TypeRuntimeWiring
import io.micronaut.context.annotation.Bean
import io.micronaut.context.annotation.Factory
import io.micronaut.core.io.ResourceResolver
import org.slf4j.LoggerFactory
import java.io.BufferedReader
import java.io.InputStreamReader
import jakarta.inject.Singleton

@Factory // <1>
class GraphQLFactory {

    @Bean
    @Singleton
    fun graphQL(resourceResolver: ResourceResolver, graphQLDataFetchers: GraphQLDataFetchers): GraphQL {
        val schemaParser = SchemaParser() // <2>

        val typeRegistry = TypeDefinitionRegistry()
        val graphqlSchema = resourceResolver.getResourceAsStream("classpath:schema.graphqls") // <3>

        return if (graphqlSchema.isPresent) {
            typeRegistry.merge(schemaParser.parse(BufferedReader(InputStreamReader(graphqlSchema.get())))) // <4>
            val runtimeWiring = RuntimeWiring.newRuntimeWiring() // <5>
                .type(TypeRuntimeWiring.newTypeWiring("Query")
                        .dataFetcher("bookById", graphQLDataFetchers.bookByIdDataFetcher())) // <6>
                .type(TypeRuntimeWiring.newTypeWiring("Book")
                        .dataFetcher("author", graphQLDataFetchers.authorDataFetcher())) // <7>
                .build()
            val schemaGenerator = SchemaGenerator()
            val graphQLSchema = schemaGenerator.makeExecutableSchema(typeRegistry, runtimeWiring) // <8>
            GraphQL.newGraphQL(graphQLSchema).build() // <9>
        } else {
            LOG.debug("No GraphQL services found, returning empty schema")
            GraphQL.Builder(GraphQLSchema.newSchema().build()).build()
        }
    }

    companion object {
        private val LOG = LoggerFactory.getLogger(GraphQLFactory::class.java)
    }
}