package example.micronaut

import example.micronaut.domain.StudentScheduleClassSubView
import example.micronaut.domain.StudentScheduleSubView
import example.micronaut.domain.StudentView
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import jakarta.inject.Inject
import spock.lang.Specification

@MicronautTest
class StudentViewRepositorySpec extends Specification {

    @Inject
    StudentViewRepository studentViewRepository

    @Inject
    ClassRepository classRepository

    def "test create student view"() {
        when:
        def mathClass = classRepository.save(new example.micronaut.domain.Class(name: "Math"))
        def studentScheduleClassSubView = new StudentScheduleClassSubView(classID: mathClass.id, name: mathClass.name)
        def studentScheduleSubView = new StudentScheduleSubView(id: 0L, clazz: studentScheduleClassSubView)
        def studentView = new StudentView(name: "John", classes: [studentScheduleSubView], extras: [department: "Research", remote: true])
        studentViewRepository.save(studentView)
        def student = studentViewRepository.findByName(studentView.name)

        then:
        student.isPresent()
        student.get().extras.department == "Research"
        student.get().extras.remote == true
        student.get().classes.size() == 1
        student.get().classes[0].clazz.name == "Math"
    }
}
