from graphql import GraphQL
from jakarta.inject import Singleton
from micronaut.context.annotation import Factory
from micronaut.graphql import create_graphql

from .graph_ql_data_fetchers import GraphQLDataFetchers
from .schema import SCHEMA


@Factory  # <1>
class GraphQLFactory:
    @Singleton
    def graph_ql(
        self,
        graph_ql_data_fetchers: GraphQLDataFetchers,
    ) -> GraphQL:
        return create_graphql(  # <2>
            SCHEMA,  # <3>
            bindings={
                "Query": {"bookById": graph_ql_data_fetchers.book_by_id},  # <4>
                "Book": {  # <5>
                    "author": graph_ql_data_fetchers.author,
                    "pageCount": "page_count",
                },
                "Author": {"firstName": "first_name", "lastName": "last_name"},  # <6>
            },
        )  # <7>
