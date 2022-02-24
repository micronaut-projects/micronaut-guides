package example.micronaut.genre

import example.micronaut.domain.Genre
import groovy.transform.CompileStatic
import jakarta.inject.Singleton
import org.apache.ibatis.session.SqlSession
import org.apache.ibatis.session.SqlSessionFactory

import javax.validation.constraints.NotNull
import javax.validation.constraints.Pattern
import javax.validation.constraints.Positive
import javax.validation.constraints.PositiveOrZero

@CompileStatic
@Singleton // <1>
class GenreMapperImpl implements GenreMapper {

    private final SqlSessionFactory sqlSessionFactory // <2>

    GenreMapperImpl(SqlSessionFactory sqlSessionFactory) {
        this.sqlSessionFactory = sqlSessionFactory // <2>
    }

    @Override
    Genre findById(long id) {
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) { // <3>
            return getGenreMapper(sqlSession).findById(id) // <5>
        }
    }

    @Override
    void save(Genre genre) {
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
            getGenreMapper(sqlSession).save(genre)
            sqlSession.commit() // <6>
        }
    }

    @Override
    void deleteById(long id) {
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
            getGenreMapper(sqlSession).deleteById(id)
            sqlSession.commit()
        }
    }

    @Override
    void update(long id, String name) {
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
            getGenreMapper(sqlSession).update(id, name)
            sqlSession.commit()
        }
    }

    @Override
    List<Genre> findAll() {
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
            return getGenreMapper(sqlSession).findAll()
        }
    }

    @Override
    List<Genre> findAllBySortAndOrder(@NotNull @Pattern(regexp = 'id|name') String sort,
                                      @NotNull @Pattern(regexp = 'asc|ASC|desc|DESC') String order) {
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
            return getGenreMapper(sqlSession).findAllBySortAndOrder(sort, order)
        }
    }

    @Override
    List<Genre> findAllByOffsetAndMaxAndSortAndOrder(@PositiveOrZero int offset,
                                                     @Positive int max,
                                                     @NotNull @Pattern(regexp = 'id|name') String sort,
                                                     @NotNull @Pattern(regexp = 'asc|ASC|desc|DESC') String order) {
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
            return getGenreMapper(sqlSession).findAllByOffsetAndMaxAndSortAndOrder(offset, max, sort, order)
        }
    }

    @Override
    List<Genre> findAllByOffsetAndMax(@PositiveOrZero int offset,
                                      @Positive int max) {
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
            return getGenreMapper(sqlSession).findAllByOffsetAndMax(offset, max)
        }
    }

    private GenreMapper getGenreMapper(SqlSession sqlSession) {
        sqlSession.getMapper(GenreMapper) // <4>
    }
}
