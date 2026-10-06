from micronaut.context.annotation import ConfigurationProperties


@ConfigurationProperties("db")  # <1>
class MongoDbConfiguration:
    name: str
    collection: str
