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

import io.micronaut.core.type.Argument
import io.micronaut.http.HttpRequest
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import io.micronaut.transaction.TransactionOperations
import jakarta.inject.Inject
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.sql.Connection

@MicronautTest(transactional = false) // <1>
class OracleBooleanTest {

    @Inject
    lateinit var taskRepository: TaskRepository

    @Inject
    lateinit var transactionOperations: TransactionOperations<Connection>

    @Inject
    @field:Client("/")
    lateinit var httpClient: HttpClient

    @BeforeEach
    fun deleteTasks() {
        taskRepository.deleteAll()
    }

    @Test
    fun completedColumnIsNativeBoolean() {
        assertEquals("BOOLEAN", columnDataType("TASK", "COMPLETED")) // <2>
    }

    @Test
    fun queriesBooleanValues() {
        val write = taskRepository.save(Task(title = "Write the guide"))
        val review = taskRepository.save(Task(title = "Review the guide"))

        assertEquals(1L, taskRepository.updateCompleted(write.id!!, true)) // <3>

        assertEquals(listOf("Write the guide"), taskRepository.findByCompletedTrue().map { it.title }) // <4>
        assertEquals(listOf("Review the guide"), taskRepository.findByCompletedFalse().map { it.title })
        assertEquals(listOf("Write the guide"), taskRepository.findByCompleted(true).map { it.title }) // <5>

        assertTrue(taskRepository.findById(write.id!!).orElseThrow().completed)
        assertFalse(taskRepository.findById(review.id!!).orElseThrow().completed)
    }

    @Test
    fun completesTaskOverHttp() {
        val client = httpClient.toBlocking()
        val task = client.retrieve(HttpRequest.POST("/tasks", mapOf("title" to "Publish the guide")), Task::class.java)
        assertFalse(task.completed)

        val completed = client.retrieve(HttpRequest.PUT("/tasks/${task.id}/complete", ""), Task::class.java) // <6>
        assertTrue(completed.completed)

        assertEquals(listOf("Publish the guide"),
            client.retrieve(HttpRequest.GET<Any>("/tasks/completed"), Argument.listOf(Task::class.java)).map { it.title })
        assertTrue(client.retrieve(HttpRequest.GET<Any>("/tasks/open"), Argument.listOf(Task::class.java)).isEmpty())
    }

    private fun columnDataType(table: String, column: String): String? =
        transactionOperations.executeRead { status ->
            val sql = "SELECT data_type FROM user_tab_columns WHERE table_name = ? AND column_name = ?"
            status.connection.prepareStatement(sql).use { statement ->
                statement.setString(1, table)
                statement.setString(2, column)
                statement.executeQuery().use { resultSet ->
                    if (resultSet.next()) resultSet.getString(1) else null
                }
            }
        }
}
