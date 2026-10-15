package example.micronaut.domain

import com.fasterxml.jackson.annotation.JsonAnyGetter
import com.fasterxml.jackson.annotation.JsonAnySetter
import io.micronaut.data.annotation.GeneratedValue
import io.micronaut.data.annotation.Id
import io.micronaut.data.annotation.MappedEntity
import io.micronaut.data.annotation.Relation
import io.micronaut.data.annotation.sql.JoinTable

@MappedEntity(value = "TBL_STUDENT", alias = "s") // <1>
class Student {
    @Id // <2>
    @GeneratedValue(GeneratedValue.Type.IDENTITY) // <3>
    Long id
    String name

    @JoinTable(name = "TBL_STUDENT_CLASSES", alias = "sc") // <4>
    @Relation(Relation.Kind.MANY_TO_MANY) // <5>
    List<Class> classes

    @JsonAnyGetter // <6>
    @JsonAnySetter
    private Map<String, Object> extras

    Map<String, Object> getExtras() {
        extras
    }

    void setExtras(Map<String, Object> extras) {
        this.extras = extras
    }
}
