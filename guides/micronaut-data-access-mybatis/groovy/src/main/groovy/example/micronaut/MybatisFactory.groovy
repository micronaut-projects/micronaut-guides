package example.micronaut

import groovy.transform.CompileStatic
import io.micronaut.context.annotation.Factory
import jakarta.inject.Singleton
import org.apache.ibatis.mapping.Environment
import org.apache.ibatis.session.Configuration
import org.apache.ibatis.session.SqlSessionFactory
import org.apache.ibatis.session.SqlSessionFactoryBuilder
import org.apache.ibatis.transaction.TransactionFactory
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory

import javax.sql.DataSource

@CompileStatic
@Factory // <1>
class MybatisFactory {

    private final DataSource dataSource // <2>

    MybatisFactory(DataSource dataSource) {
        this.dataSource = dataSource // <2>
    }

    @Singleton // <3>
    SqlSessionFactory sqlSessionFactory() {
        TransactionFactory transactionFactory = new JdbcTransactionFactory()

        Environment environment = new Environment('dev', transactionFactory, dataSource) // <4>
        Configuration configuration = new Configuration(environment)
        configuration.addMappers('example.micronaut') // <5>

        new SqlSessionFactoryBuilder().build(configuration) // <6>
    }

}
