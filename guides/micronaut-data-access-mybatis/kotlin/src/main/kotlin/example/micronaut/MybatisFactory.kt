/*
 * Copyright 2017-2023 original authors
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

import io.micronaut.context.annotation.Factory
import jakarta.inject.Singleton
import org.apache.ibatis.mapping.Environment
import org.apache.ibatis.session.Configuration
import org.apache.ibatis.session.SqlSessionFactory
import org.apache.ibatis.session.SqlSessionFactoryBuilder
import org.apache.ibatis.transaction.TransactionFactory
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory
import javax.sql.DataSource

@Factory // <1>
class MybatisFactory(private val dataSource: DataSource) { // <2>

    @Singleton // <3>
    fun sqlSessionFactory(): SqlSessionFactory {
        val transactionFactory: TransactionFactory = JdbcTransactionFactory()
        val environment = Environment("dev", transactionFactory, dataSource) // <4>
        val configuration = Configuration(environment)
        configuration.addMappers("example.micronaut") // <5>
        return SqlSessionFactoryBuilder().build(configuration) // <6>
    }
}
