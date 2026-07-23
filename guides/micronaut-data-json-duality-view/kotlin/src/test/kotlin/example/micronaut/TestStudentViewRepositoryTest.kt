package example.micronaut

import example.micronaut.domain.Class
import example.micronaut.domain.StudentScheduleClassSubView
import example.micronaut.domain.StudentScheduleSubView
import example.micronaut.domain.StudentView
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import jakarta.inject.Inject
import org.junit.jupiter.api.Test
import java.util.*
import kotlin.test.assertNotNull

@MicronautTest
class TestStudentViewRepositoryTest {

    @Inject
    lateinit var studentViewRepository: StudentViewRepository

    @Inject
    lateinit var classRepository: ClassRepository

    @Test
    fun testCreateStudentView() {
        val mathClass = classRepository.save(Class(null, "Math"))
        val studentView = StudentView(null, "John", mutableListOf())
        val studentScheduleClassSubView = StudentScheduleClassSubView(null, mathClass.name)
        val studentScheduleSubView = StudentScheduleSubView(null, studentScheduleClassSubView)
        studentViewRepository.save(studentView)
        val student: Optional<StudentView> = studentViewRepository.findByName(studentView.name)
        assertNotNull(student)
    }
}
