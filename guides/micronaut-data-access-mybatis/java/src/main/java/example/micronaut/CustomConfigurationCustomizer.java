package example.micronaut;

import io.micronaut.configuration.mybatis.MyBatisConfigurationCustomizer;
import jakarta.inject.Named;
import jakarta.inject.Singleton;
import org.apache.ibatis.session.Configuration;
import example.micronaut.genre.GenreMapper;

@Named("default") // <1>
@Singleton // <2>
public class CustomConfigurationCustomizer implements MyBatisConfigurationCustomizer {
    @Override
    public void customize(Configuration configuration) {
        configuration.addMapper(GenreMapper.class); // <3>
    }
}
