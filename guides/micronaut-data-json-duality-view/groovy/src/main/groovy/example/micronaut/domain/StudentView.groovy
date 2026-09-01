package example.micronaut.domain

import com.fasterxml.jackson.annotation.JsonAnyGetter
import com.fasterxml.jackson.annotation.JsonAnySetter
import io.micronaut.data.annotation.GeneratedValue
import io.micronaut.data.annotation.Id
import io.micronaut.data.annotation.JsonView
import io.micronaut.data.annotation.Relation

@JsonView(entity = Student) // <1>
class StudentView {
    @Id // <2>
    @GeneratedValue(GeneratedValue.Type.IDENTITY) // <3>
    Long id

    String name

    @Relation(Relation.Kind.ONE_TO_MANY) // <4>
    List<StudentScheduleSubView> classes

    @JsonAnyGetter // <5>
    @JsonAnySetter
    private Map<String, Object> extras

    Map<String, Object> getExtras() {
        extras
    }

    void setExtras(Map<String, Object> extras) {
        this.extras = extras
    }
}
