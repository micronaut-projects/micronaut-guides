/*
 * Copyright 2017-2026 original authors
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
package example.micronaut

import jakarta.inject.Singleton
import jakarta.validation.Valid
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import org.slf4j.LoggerFactory
import software.amazon.awssdk.services.dynamodb.DynamoDbClient
import software.amazon.awssdk.services.dynamodb.model.AttributeValue
import software.amazon.awssdk.services.dynamodb.model.PutItemRequest
import software.amazon.awssdk.services.dynamodb.model.QueryRequest
import software.amazon.awssdk.services.dynamodb.model.QueryResponse
import java.util.Optional

@Singleton
open class DefaultBookRepository(
    dynamoDbClient: DynamoDbClient,
    dynamoConfiguration: DynamoConfiguration
) : DynamoRepository<Book>(dynamoDbClient, dynamoConfiguration), BookRepository {

    override fun save(@NotNull @Valid book: Book) {
        val itemResponse = dynamoDbClient.putItem(
            PutItemRequest.builder()
                .tableName(dynamoConfiguration.tableName)
                .item(item(book))
                .build()
        )
        if (LOG.isDebugEnabled) {
            LOG.debug(itemResponse.toString())
        }
    }

    override fun findById(@NotBlank id: String): Optional<Book> =
        findById(Book::class.java, id).map { bookOf(it) }

    override fun delete(@NotBlank id: String) {
        delete(Book::class.java, id)
    }

    override fun findAll(): List<Book> {
        val result = mutableListOf<Book>()
        var beforeId: String? = null
        do {
            val request: QueryRequest = findAllQueryRequest(Book::class.java, beforeId, null)
            val response: QueryResponse = dynamoDbClient.query(request)
            if (LOG.isTraceEnabled) {
                LOG.trace(response.toString())
            }
            result.addAll(parseInResponse(response))
            beforeId = lastEvaluatedId(response, Book::class.java).orElse(null)
            LOG.trace("Before ID {}", beforeId)
        } while (beforeId != null)
        return result
    }

    private fun parseInResponse(response: QueryResponse): List<Book> =
        response.items().map { bookOf(it) }

    private fun bookOf(item: Map<String, AttributeValue>): Book =
        Book(
            item.getValue(ATTRIBUTE_ID).s(),
            item.getValue(ATTRIBUTE_ISBN).s(),
            item.getValue(ATTRIBUTE_NAME).s(),
            item[ATTRIBUTE_STOCK]?.n()?.toInt()
        )

    override fun item(entity: Book): MutableMap<String, AttributeValue> =
        super.item(entity).also {
            it[ATTRIBUTE_ID] = AttributeValue.builder().s(entity.id).build()
            it[ATTRIBUTE_ISBN] = AttributeValue.builder().s(entity.isbn).build()
            it[ATTRIBUTE_NAME] = AttributeValue.builder().s(entity.name).build()
            if (entity.stock != null) {
                it[ATTRIBUTE_STOCK] = AttributeValue.builder().n(entity.stock.toString()).build()
            }
            it[ATTRIBUTE_GSI_2_PK] = AttributeValue.builder().s(PREFIX_ISBN + HASH + entity.isbn).build()
            it[ATTRIBUTE_GSI_2_SK] = it.getValue(ATTRIBUTE_PK)
        }

    companion object {
        private val LOG = LoggerFactory.getLogger(DefaultBookRepository::class.java)
        private const val ATTRIBUTE_ID = "id"
        private const val ATTRIBUTE_ISBN = "isbn"
        private const val ATTRIBUTE_NAME = "name"
        private const val ATTRIBUTE_STOCK = "stock"
        private const val PREFIX_ISBN = "ISBN"
    }
}
