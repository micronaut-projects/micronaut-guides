from graphql import GraphQL
from graphql.schema.idl import RuntimeWiring, SchemaGenerator, SchemaParser, TypeDefinitionRegistry
from graphql.schema.idl import TypeRuntimeWiring
from jakarta.inject import Singleton
from java.io import BufferedReader, InputStreamReader
from micronaut.context.annotation import Factory
from micronaut.core.io import ResourceResolver

from .author_data_fetcher import AuthorDataFetcher
from .complete_to_do_data_fetcher import CompleteToDoDataFetcher
from .create_to_do_data_fetcher import CreateToDoDataFetcher
from .to_dos_data_fetcher import ToDosDataFetcher


@Factory  # <1>
class GraphQLFactory:
    @Singleton  # <2>
    def graph_ql(
        self,
        resource_resolver: ResourceResolver,
        to_dos_data_fetcher: ToDosDataFetcher,
        create_to_do_data_fetcher: CreateToDoDataFetcher,
        complete_to_do_data_fetcher: CompleteToDoDataFetcher,
        author_data_fetcher: AuthorDataFetcher,
    ) -> GraphQL:
        schema_parser = SchemaParser()
        schema_generator = SchemaGenerator()

        schema_definition = resource_resolver.getResourceAsStream(
            "classpath:schema.graphqls"
        ).get()

        type_registry = TypeDefinitionRegistry()
        type_registry.merge(
            schema_parser.parse(BufferedReader(InputStreamReader(schema_definition)))
        )

        runtime_wiring = (
            RuntimeWiring.newRuntimeWiring()
            .type(
                TypeRuntimeWiring.newTypeWiring("Query").dataFetcher(
                    "toDos",
                    to_dos_data_fetcher,
                )
            )  # <3>
            .type(
                TypeRuntimeWiring.newTypeWiring("Mutation")
                .dataFetcher("createToDo", create_to_do_data_fetcher)
                .dataFetcher("completeToDo", complete_to_do_data_fetcher)
            )  # <4>
            .type(
                TypeRuntimeWiring.newTypeWiring("ToDo").dataFetcher(
                    "author",
                    author_data_fetcher,
                )
            )  # <5>
            .build()
        )

        graph_ql_schema = schema_generator.makeExecutableSchema(
            type_registry,
            runtime_wiring,
        )
        return GraphQL.newGraphQL(graph_ql_schema).build()
