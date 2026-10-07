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
import io.micronaut.http.client.BlockingHttpClient
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import io.micronaut.transaction.TransactionOperations
import jakarta.inject.Inject
import spock.lang.Specification

import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.ResultSet

@MicronautTest(transactional = false) // <1>
class OracleBooleanSpec extends Specification {

    @Inject
    TaskRepository taskRepository

    @Inject
    TransactionOperations<Connection> transactionOperations

    @Inject
    @Client('/')
    HttpClient httpClient

    void setup() {
        taskRepository.deleteAll()
    }

    void 'completed column is native BOOLEAN'() {
        expect:
        columnDataType('TASK', 'COMPLETED') == 'BOOLEAN' // <2>
    }

    void 'queries boolean values'() {
        given:
        Task write = taskRepository.save(new Task(null, 'Write the guide', false))
        Task review = taskRepository.save(new Task(null, 'Review the guide', false))

        expect:
        taskRepository.updateCompleted(write.id, true) == 1L // <3>

        taskRepository.findByCompletedTrue()*.title == ['Write the guide'] // <4>
        taskRepository.findByCompletedFalse()*.title == ['Review the guide']
        taskRepository.findByCompleted(true)*.title == ['Write the guide'] // <5>

        taskRepository.findById(write.id).orElseThrow().completed
        !taskRepository.findById(review.id).orElseThrow().completed
    }

    void 'completes a task over HTTP'() {
        given:
        BlockingHttpClient client = httpClient.toBlocking()

        when:
        Task task = client.retrieve(HttpRequest.POST('/tasks', [title: 'Publish the guide']), Task)

        then:
        !task.completed

        when:
        Task completed = client.retrieve(HttpRequest.PUT("/tasks/${task.id}/complete", ''), Task) // <6>

        then:
        completed.completed
        client.retrieve(HttpRequest.GET('/tasks/completed'), Argument.listOf(Task))*.title == ['Publish the guide']
        client.retrieve(HttpRequest.GET('/tasks/open'), Argument.listOf(Task)).isEmpty()
    }

    private String columnDataType(String table, String column) {
        transactionOperations.executeRead { status ->
            String sql = 'SELECT data_type FROM user_tab_columns WHERE table_name = ? AND column_name = ?'
            PreparedStatement statement = status.connection.prepareStatement(sql)
            try {
                statement.setString(1, table)
                statement.setString(2, column)
                ResultSet resultSet = statement.executeQuery()
                try {
                    return resultSet.next() ? resultSet.getString(1) : null
                } finally {
                    resultSet.close()
                }
            } finally {
                statement.close()
            }
        }
    }
}
