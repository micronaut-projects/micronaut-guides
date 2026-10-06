from graphql import GraphQL
from graphql.schema import PropertyDataFetcher
from graphql.schema.idl import RuntimeWiring, SchemaGenerator, SchemaParser
from graphql.schema.idl import TypeRuntimeWiring
from jakarta.inject import Singleton
from micronaut.context.annotation import Factory

from .graph_ql_data_fetchers import GraphQLDataFetchers
from .schema import SCHEMA


@Factory  # <1>
class GraphQLFactory:
    @Singleton
    def graph_ql(
        self,
        graph_ql_data_fetchers: GraphQLDataFetchers,
    ) -> GraphQL:
        schema_parser = SchemaParser()  # <2>

        graphql_schema = SCHEMA  # <3>
        type_registry = schema_parser.parse(graphql_schema)  # <4>

        runtime_wiring = (
            RuntimeWiring.newRuntimeWiring()  # <5>
            .type(
                TypeRuntimeWiring.newTypeWiring("Query").dataFetcher(
                    "bookById",
                    graph_ql_data_fetchers.book_by_id,
                )
            )  # <6>
            .type(
                TypeRuntimeWiring.newTypeWiring("Book")
                .dataFetcher("author", graph_ql_data_fetchers.author)
                .dataFetcher(
                    "pageCount", PropertyDataFetcher.fetching("page_count")
                )
            )  # <7>
            .type(
                TypeRuntimeWiring.newTypeWiring("Author")
                .dataFetcher("firstName", PropertyDataFetcher.fetching("first_name"))
                .dataFetcher("lastName", PropertyDataFetcher.fetching("last_name"))
            )  # <8>
            .build()
        )

        schema_generator = SchemaGenerator()
        graph_ql_schema = schema_generator.makeExecutableSchema(
            type_registry,
            runtime_wiring,
        )  # <9>

        return GraphQL.newGraphQL(graph_ql_schema).build()  # <10>
