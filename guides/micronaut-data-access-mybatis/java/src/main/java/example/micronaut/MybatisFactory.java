package example.micronaut;

import io.micronaut.context.annotation.Bean;
import io.micronaut.context.annotation.Factory;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.transaction.TransactionFactory;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;

import javax.inject.Singleton;
import javax.sql.DataSource;

@Factory // <1>
public class MybatisFactory {

    private final DataSource dataSource; // <2>

    public MybatisFactory(DataSource dataSource) {
        this.dataSource = dataSource; // <2>
    }

    @Singleton // <3>
    public SqlSessionFactory sqlSessionFactory() {
        TransactionFactory transactionFactory = new JdbcTransactionFactory();

        Environment environment = new Environment("dev", transactionFactory, dataSource); // <4>
        Configuration configuration = new Configuration(environment);
        configuration.addMappers("example.micronaut"); // <5>

        return new SqlSessionFactoryBuilder().build(configuration); // <6>
    }

}
