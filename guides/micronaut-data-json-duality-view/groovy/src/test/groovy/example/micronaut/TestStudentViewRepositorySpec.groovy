package example.micronaut

import example.micronaut.domain.Class
import example.micronaut.domain.StudentScheduleClassSubView
import example.micronaut.domain.StudentScheduleSubView
import example.micronaut.domain.StudentView
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import jakarta.inject.Inject
import spock.lang.Specification

@MicronautTest
class TestStudentViewRepositorySpec extends Specification {

    @Inject
    StudentViewRepository studentViewRepository

    @Inject
    ClassRepository classRepository

    def "test create student view"() {
        when:
        def mathClass = classRepository.save(new Class(name: "Math"))
        def studentView = new StudentView(name: "John", classes: [])
        def studentScheduleClassSubView = new StudentScheduleClassSubView(classID: null, name: mathClass.name)
        def studentScheduleSubView = new StudentScheduleSubView(id: 0L, clazz: studentScheduleClassSubView)
        studentViewRepository.save(studentView)
        def student = studentViewRepository.findByName(studentView.name)

        then:
        student != null
    }
}
