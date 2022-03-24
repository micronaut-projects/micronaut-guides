package example.micronaut

import example.micronaut.domain.Student

class StudentCourseCreationSpec extends BaseMongoDataSpec {

    def "we begin with no students"() {
        when:
        def students = studentClient.list()

        then:
        students.empty
    }

    def "and no courses"() {
        when:
        def courses = courseClient.list()

        then:
        courses.empty
    }

    def "courses can be created"() {
        when:
        def math = courseClient.create("Math")
        def physics = courseClient.create("Physics")

        then:
        math.name == "Math"
        math.id != null

        and:
        physics.name == "Physics"
        physics.id != null

        and:
        courseClient.list().size() == 2
    }

    def "students can be created"() {
        when:
        def math = courseClient.create("Math")
        def art = courseClient.create("Art")
        def history = courseClient.create("History")

        def tim = studentClient.create(new Student("Tim", [math, art]))
        def sarah = studentClient.create(new Student("Sarah", [history, math]))

        then: "they are correctly populated"
        courseClient.list().size() == 3
        studentClient.list().size() == 2

        when:
        def result = studentClient.find(sarah.id)

        then:
        with(result.get()) {
            it.name == "Sarah"
            it.courses.size() == 2
            it.courses.find { it.name == "Math" } != null
            it.courses.find { it.name == "History" } != null
        }

        when:
        result = studentClient.find(tim.id)

        then:
        with(result.get()) {
            it.name == "Tim"
            it.courses.size() == 2
            it.courses.find { it.name == "Math" } != null
            it.courses.find { it.name == "Art" } != null
        }

        and:
        courseClient.get(math.id).get().students.size() == 2
        courseClient.get(art.id).get().students.size() == 1
        courseClient.get(history.id).get().students.size() == 1
    }
}
