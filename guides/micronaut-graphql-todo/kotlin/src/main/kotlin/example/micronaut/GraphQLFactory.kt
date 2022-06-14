package example.micronaut

import graphql.GraphQL
import graphql.schema.idl.*
import graphql.schema.idl.errors.SchemaMissingError
import io.micronaut.context.annotation.Factory
import io.micronaut.core.io.ResourceResolver
import jakarta.inject.Singleton
import java.io.BufferedReader
import java.io.InputStreamReader

@Factory // <1>
class GraphQLFactory {

    @Singleton // <2>
    fun graphQL(
        resourceResolver: ResourceResolver,
        toDosDataFetcher: ToDosDataFetcher,
        createToDoDataFetcher: CreateToDoDataFetcher,
        completeToDoDataFetcher: CompleteToDoDataFetcher,
        authorDataFetcher: AuthorDataFetcher
    ): GraphQL {
        val schemaParser = SchemaParser()
        val schemaGenerator = SchemaGenerator()

        // Load the schema
        val schemaDefinition = resourceResolver
            .getResourceAsStream("classpath:schema.graphqls")
            .orElseThrow { SchemaMissingError() }

        // Parse the schema and merge it into a type registry
        val typeRegistry = TypeDefinitionRegistry()
        typeRegistry.merge(schemaParser.parse(BufferedReader(InputStreamReader(schemaDefinition))))

        // Create the runtime wiring.
        val runtimeWiring = RuntimeWiring.newRuntimeWiring()
            .type("Query") { typeWiring: TypeRuntimeWiring.Builder ->  // <3>
                typeWiring
                    .dataFetcher("toDos", toDosDataFetcher)
            }
            .type("Mutation") { typeWiring: TypeRuntimeWiring.Builder ->  // <4>
                typeWiring
                    .dataFetcher("createToDo", createToDoDataFetcher)
                    .dataFetcher("completeToDo", completeToDoDataFetcher)
            }
            .type("ToDo") { typeWiring: TypeRuntimeWiring.Builder ->  // <5>
                typeWiring
                    .dataFetcher("author", authorDataFetcher)
            }
            .build()

        // Create the executable schema.
        val graphQLSchema = schemaGenerator.makeExecutableSchema(typeRegistry, runtimeWiring)

        // Return the GraphQL bean.
        return GraphQL.newGraphQL(graphQLSchema).build()
    }
}