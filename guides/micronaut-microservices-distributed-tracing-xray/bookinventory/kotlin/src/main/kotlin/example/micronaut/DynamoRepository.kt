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

import io.micronaut.context.annotation.Requires
import jakarta.inject.Singleton
import org.slf4j.LoggerFactory
import software.amazon.awssdk.services.dynamodb.DynamoDbClient
import software.amazon.awssdk.services.dynamodb.model.AttributeDefinition
import software.amazon.awssdk.services.dynamodb.model.AttributeValue
import software.amazon.awssdk.services.dynamodb.model.BillingMode
import software.amazon.awssdk.services.dynamodb.model.CreateTableRequest
import software.amazon.awssdk.services.dynamodb.model.DeleteItemRequest
import software.amazon.awssdk.services.dynamodb.model.DescribeTableRequest
import software.amazon.awssdk.services.dynamodb.model.GlobalSecondaryIndex
import software.amazon.awssdk.services.dynamodb.model.KeySchemaElement
import software.amazon.awssdk.services.dynamodb.model.KeyType
import software.amazon.awssdk.services.dynamodb.model.Projection
import software.amazon.awssdk.services.dynamodb.model.ProjectionType
import software.amazon.awssdk.services.dynamodb.model.QueryRequest
import software.amazon.awssdk.services.dynamodb.model.QueryResponse
import software.amazon.awssdk.services.dynamodb.model.ResourceNotFoundException
import software.amazon.awssdk.services.dynamodb.model.ScalarAttributeType
import java.util.Optional

@Requires(condition = CIAwsRegionProviderChainCondition::class)
@Requires(condition = CIAwsCredentialsProviderChainCondition::class)
@Requires(beans = [DynamoConfiguration::class, DynamoDbClient::class])
@Singleton
abstract class DynamoRepository<T : Identified>(
    protected val dynamoDbClient: DynamoDbClient,
    protected val dynamoConfiguration: DynamoConfiguration
) {
    fun existsTable(): Boolean =
        try {
            dynamoDbClient.describeTable(
                DescribeTableRequest.builder()
                    .tableName(dynamoConfiguration.tableName)
                    .build()
            )
            true
        } catch (e: ResourceNotFoundException) {
            false
        }

    fun createTable() {
        dynamoDbClient.createTable(
            CreateTableRequest.builder()
                .attributeDefinitions(
                    AttributeDefinition.builder().attributeName(ATTRIBUTE_PK).attributeType(ScalarAttributeType.S).build(),
                    AttributeDefinition.builder().attributeName(ATTRIBUTE_SK).attributeType(ScalarAttributeType.S).build(),
                    AttributeDefinition.builder().attributeName(ATTRIBUTE_GSI_1_PK).attributeType(ScalarAttributeType.S).build(),
                    AttributeDefinition.builder().attributeName(ATTRIBUTE_GSI_1_SK).attributeType(ScalarAttributeType.S).build(),
                    AttributeDefinition.builder().attributeName(ATTRIBUTE_GSI_2_PK).attributeType(ScalarAttributeType.S).build(),
                    AttributeDefinition.builder().attributeName(ATTRIBUTE_GSI_2_SK).attributeType(ScalarAttributeType.S).build()
                )
                .keySchema(
                    KeySchemaElement.builder().attributeName(ATTRIBUTE_PK).keyType(KeyType.HASH).build(),
                    KeySchemaElement.builder().attributeName(ATTRIBUTE_SK).keyType(KeyType.RANGE).build()
                )
                .billingMode(BillingMode.PAY_PER_REQUEST)
                .tableName(dynamoConfiguration.tableName)
                .globalSecondaryIndexes(gsi1(), gsi2())
                .build()
        )
    }

    fun findAllQueryRequest(cls: Class<*>, beforeId: String?, limit: Int?): QueryRequest {
        val builder = QueryRequest.builder()
            .tableName(dynamoConfiguration.tableName)
            .indexName(INDEX_GSI_1)
            .scanIndexForward(false)
        if (limit != null) {
            builder.limit(limit)
        }
        return if (beforeId == null) {
            builder.keyConditionExpression("#pk = :pk")
                .expressionAttributeNames(mapOf("#pk" to ATTRIBUTE_GSI_1_PK))
                .expressionAttributeValues(mapOf(":pk" to classAttributeValue(cls)))
                .build()
        } else {
            builder.keyConditionExpression("#pk = :pk and #sk < :sk")
                .expressionAttributeNames(mapOf("#pk" to ATTRIBUTE_GSI_1_PK, "#sk" to ATTRIBUTE_GSI_1_SK))
                .expressionAttributeValues(mapOf(":pk" to classAttributeValue(cls), ":sk" to id(cls, beforeId)))
                .build()
        }
    }

    protected fun delete(cls: Class<*>, id: String) {
        val pk = id(cls, id)
        val deleteItemResponse = dynamoDbClient.deleteItem(
            DeleteItemRequest.builder()
                .tableName(dynamoConfiguration.tableName)
                .key(mapOf(ATTRIBUTE_PK to pk, ATTRIBUTE_SK to pk))
                .build()
        )
        if (LOG.isDebugEnabled) {
            LOG.debug(deleteItemResponse.toString())
        }
    }

    protected fun findById(cls: Class<*>, id: String): Optional<Map<String, AttributeValue>> {
        val pk = id(cls, id)
        val getItemResponse = dynamoDbClient.getItem {
            it.tableName(dynamoConfiguration.tableName)
                .key(mapOf(ATTRIBUTE_PK to pk, ATTRIBUTE_SK to pk))
        }
        return if (!getItemResponse.hasItem()) {
            Optional.empty()
        } else {
            Optional.of(getItemResponse.item())
        }
    }

    open fun item(entity: T): MutableMap<String, AttributeValue> {
        val item = mutableMapOf<String, AttributeValue>()
        val pk = id(entity.javaClass, entity.id)
        item[ATTRIBUTE_PK] = pk
        item[ATTRIBUTE_SK] = pk
        item[ATTRIBUTE_GSI_1_PK] = classAttributeValue(entity.javaClass)
        item[ATTRIBUTE_GSI_1_SK] = pk
        return item
    }

    companion object {
        private val LOG = LoggerFactory.getLogger(DynamoRepository::class.java)
        protected const val HASH = "#"
        protected const val ATTRIBUTE_PK = "pk"
        protected const val ATTRIBUTE_SK = "sk"
        protected const val ATTRIBUTE_GSI_1_PK = "GSI1PK"
        protected const val ATTRIBUTE_GSI_1_SK = "GSI1SK"
        protected const val ATTRIBUTE_GSI_2_PK = "GS21PK"
        protected const val ATTRIBUTE_GSI_2_SK = "GS21SK"
        protected const val INDEX_GSI_1 = "GSI1"
        protected const val INDEX_GSI_2 = "GSI2"

        fun lastEvaluatedId(response: QueryResponse, cls: Class<*>): Optional<String> {
            if (response.hasLastEvaluatedKey()) {
                val item = response.lastEvaluatedKey()
                if (item != null && item.containsKey(ATTRIBUTE_PK)) {
                    return id(cls, item.getValue(ATTRIBUTE_PK))
                }
            }
            return Optional.empty()
        }

        private fun gsi2(): GlobalSecondaryIndex =
            gsi(INDEX_GSI_2, ATTRIBUTE_GSI_2_PK, ATTRIBUTE_GSI_2_SK)

        private fun gsi1(): GlobalSecondaryIndex =
            gsi(INDEX_GSI_1, ATTRIBUTE_GSI_1_PK, ATTRIBUTE_GSI_1_SK)

        private fun gsi(indexName: String, hashAttributeName: String, rangeAttributeName: String): GlobalSecondaryIndex =
            GlobalSecondaryIndex.builder()
                .indexName(indexName)
                .keySchema(
                    KeySchemaElement.builder().attributeName(hashAttributeName).keyType(KeyType.HASH).build(),
                    KeySchemaElement.builder().attributeName(rangeAttributeName).keyType(KeyType.RANGE).build()
                )
                .projection(Projection.builder().projectionType(ProjectionType.ALL).build())
                .build()

        protected fun classAttributeValue(cls: Class<*>): AttributeValue =
            AttributeValue.builder().s(cls.simpleName).build()

        protected fun id(cls: Class<*>, id: String): AttributeValue =
            AttributeValue.builder().s(cls.simpleName.uppercase() + HASH + id).build()

        protected fun id(cls: Class<*>, attributeValue: AttributeValue): Optional<String> {
            val str = attributeValue.s()
            val substring = cls.simpleName.uppercase() + HASH
            return if (str.startsWith(substring)) {
                Optional.of(str.substring(substring.length))
            } else {
                Optional.empty()
            }
        }
    }
}
