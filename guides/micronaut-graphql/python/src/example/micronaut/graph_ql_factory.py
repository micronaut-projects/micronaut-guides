import logging

from graphql import GraphQL
from graphql.schema import GraphQLSchema
from graphql.schema.idl import RuntimeWiring, SchemaGenerator, SchemaParser, TypeDefinitionRegistry
from graphql.schema.idl import TypeRuntimeWiring
from jakarta.inject import Singleton
from java.io import BufferedReader, InputStreamReader
from micronaut.context.annotation import Factory
from micronaut.core.io import ResourceResolver

from .graph_ql_data_fetchers import GraphQLDataFetchers

LOG = logging.getLogger(__name__)


@Factory  # <1>
class GraphQLFactory:
    @Singleton
    def graph_ql(
        self,
        resource_resolver: ResourceResolver,
        graph_ql_data_fetchers: GraphQLDataFetchers,
    ) -> GraphQL:
        schema_parser = SchemaParser()  # <2>

        type_registry = TypeDefinitionRegistry()
        graphql_schema = resource_resolver.getResourceAsStream("classpath:schema.graphqls")  # <3>

        if graphql_schema.isPresent():
            type_registry.merge(
                schema_parser.parse(
                    BufferedReader(InputStreamReader(graphql_schema.get()))
                )
            )  # <4>

            runtime_wiring = (
                RuntimeWiring.newRuntimeWiring()  # <5>
                .type(
                    TypeRuntimeWiring.newTypeWiring("Query").dataFetcher(
                        "bookById",
                        graph_ql_data_fetchers.get_book_by_id_data_fetcher(),
                    )
                )  # <6>
                .type(
                    TypeRuntimeWiring.newTypeWiring("Book").dataFetcher(
                        "author",
                        graph_ql_data_fetchers.get_author_data_fetcher(),
                    )
                )  # <7>
                .build()
            )

            schema_generator = SchemaGenerator()
            graph_ql_schema = schema_generator.makeExecutableSchema(
                type_registry,
                runtime_wiring,
            )  # <8>

            return GraphQL.newGraphQL(graph_ql_schema).build()  # <9>

        LOG.debug("No GraphQL services found, returning empty schema")
        return GraphQL.Builder(GraphQLSchema.newSchema().build()).build()
