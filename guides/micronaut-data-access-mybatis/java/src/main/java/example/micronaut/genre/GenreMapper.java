package example.micronaut.genre;

import example.micronaut.domain.Genre;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

public interface GenreMapper {

    @Select("select * from genre where id=#{id}")
    Genre findById(long id);

    @Insert("insert into genre(name) values(#{name})")
    @Options(useGeneratedKeys = true)
    void save(Genre genre);

    @Delete("delete from genre where id=#{id}")
    void deleteById(long id);

    @Update("update genre set name=#{name} where id=#{id}")
    void update(@Param("id") long id, @Param("name") String name);

    @Select("select * from genre")
    List<Genre> findAll();

    @Select("select * from genre order by ${sort} ${order}")
    List<Genre> findAllBySortAndOrder(@NotNull @Pattern(regexp = "id|name") String sort,
                                      @NotNull @Pattern(regexp = "asc|ASC|desc|DESC") String order);

    @Select("select * from genre order by ${sort} ${order} limit ${offset}, ${max}")
    List<Genre> findAllByOffsetAndMaxAndSortAndOrder(@NotNull @PositiveOrZero Integer offset,
                                                     @Positive @NotNull Integer max,
                                                     @NotNull @Pattern(regexp = "id|name") String sort,
                                                     @NotNull @Pattern(regexp = "asc|ASC|desc|DESC") String order);

    @Select("select * from genre limit ${offset}, ${max}")
    List<Genre> findAllByOffsetAndMax(@NotNull @PositiveOrZero Integer offset, @Positive @NotNull Integer max);

}
