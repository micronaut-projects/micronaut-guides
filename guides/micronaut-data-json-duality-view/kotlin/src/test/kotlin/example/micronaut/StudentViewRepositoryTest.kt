package example.micronaut

import example.micronaut.domain.StudentScheduleClassSubView
import example.micronaut.domain.StudentScheduleSubView
import example.micronaut.domain.StudentView
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import jakarta.inject.Inject
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue

@MicronautTest
class StudentViewRepositoryTest {

    @Inject
    lateinit var studentViewRepository: StudentViewRepository

    @Inject
    lateinit var classRepository: ClassRepository

    @Test
    fun testCreateStudentView() {
        val mathClass = classRepository.save(example.micronaut.domain.Class(null, "Math"))
        val studentScheduleClassSubView = StudentScheduleClassSubView(mathClass.id, mathClass.name)
        val studentScheduleSubView = StudentScheduleSubView(0L, studentScheduleClassSubView)
        val studentView = StudentView(null, "John", listOf(studentScheduleSubView), mapOf("department" to "Research", "remote" to true))
        studentViewRepository.save(studentView)
        val student = studentViewRepository.findByName(studentView.name)
        assertTrue(student.isPresent)
        val savedStudent = student.orElseThrow()
        assertEquals("Research", savedStudent.extras["department"])
        assertEquals(true, savedStudent.extras["remote"])
        assertEquals(1, savedStudent.classes.size)
        assertEquals("Math", savedStudent.classes[0].clazz.name)
    }
}
