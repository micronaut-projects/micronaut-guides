from typing import Annotated

from com.mongodb.client import MongoClient
from jakarta.inject import Singleton
from jakarta.validation import Valid
from org.bson import Document

from .fruit import Fruit
from .fruit_repository import FruitRepository
from .mongo_db_configuration import MongoDbConfiguration


@Singleton  # <1>
class MongoDbFruitRepository(FruitRepository):

    def __init__(self, mongo_conf: MongoDbConfiguration, mongo_client: MongoClient):  # <2> <3>
        self.mongo_conf = mongo_conf
        self.mongo_client = mongo_client

    def save(self, fruit: Annotated[Fruit, Valid]) -> None:
        self._collection().insertOne(self._document(fruit))

    def list(self) -> list[Fruit]:
        documents = self._collection().find()
        return [self._fruit(document) for document in documents]

    def _collection(self):
        return self.mongo_client.getDatabase(self.mongo_conf.name).getCollection(
            self.mongo_conf.collection
        )

    @staticmethod
    def _document(fruit: Fruit) -> Document:
        document = Document("name", fruit.name)
        if fruit.description is not None:
            document.append("description", fruit.description)
        return document

    @staticmethod
    def _fruit(document: Document) -> Fruit:
        return Fruit(
            name=document.getString("name"),
            description=document.getString("description"),
        )
