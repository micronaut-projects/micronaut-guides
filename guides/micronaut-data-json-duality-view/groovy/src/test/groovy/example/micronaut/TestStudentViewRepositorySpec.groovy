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
        def mathClass = classRepository.save(new Class(null, "Math"))
        def studentView = new StudentView(null, "John", [])
        def studentScheduleClassSubView = new StudentScheduleClassSubView(null, mathClass.name)
        def studentScheduleSubView = new StudentScheduleSubView(null, studentScheduleClassSubView)
        studentViewRepository.save(studentView)
        def student = studentViewRepository.findByName(studentView.name)

        then:
        student != null
    }
}
