package dev.ambon.course_catalogue_service.controller

import com.ninjasquad.springmockk.MockkBean
import dev.ambon.course_catalogue_service.dto.CourseDTO
import dev.ambon.course_catalogue_service.entity.Course
import dev.ambon.course_catalogue_service.util.courseDTO
import dev.ambon.course_catalogue_service.service.CourseService
import io.mockk.every
import io.mockk.just
import io.mockk.runs
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.reactive.server.WebTestClient

@WebMvcTest(controllers = [CourseController::class])
@ActiveProfiles("test")
@AutoConfigureWebTestClient
class CourseControllerUnitTest {
    @Autowired
    lateinit var webTestClient: WebTestClient

    @MockkBean
    lateinit var courseServiceMock: CourseService

    @Test
    fun addCourse() {
        val courseDTO = CourseDTO(null, "Build Restful APIs Using Spring Boot and Kotlin", "Dilip", 1)

        every { courseServiceMock.addCourse(any())} returns courseDTO(id = 1)

        val savedCourseDTO = webTestClient.post()
            .uri("/v1/courses")
            .bodyValue(courseDTO)
            .exchange()
            .expectStatus().isCreated
            .expectBody(CourseDTO::class.java)
            .returnResult()
            .responseBody
        assertTrue {
            savedCourseDTO!!.id != null
        }
        assertTrue {
            savedCourseDTO!!.name == courseDTO.name
        }
        assertTrue {
            savedCourseDTO!!.category == courseDTO.category
        }
    }

    @Test
    fun addCourse_validation() {
        val courseDTO = CourseDTO(null, "", "", 1)

        every { courseServiceMock.addCourse(any())} returns courseDTO(id = 1)

        val response = webTestClient.post()
            .uri("/v1/courses")
            .bodyValue(courseDTO)
            .exchange()
            .expectStatus().isBadRequest
            .expectBody(String::class.java)
            .returnResult()
            .responseBody

        assertEquals("Course category must not be blank, Course name must not be blank", response)
    }

    @Test
    fun addCourse_runtime_validation() {
        val courseDTO = CourseDTO(null, "Build Restful APIs Using Spring Boot and Kotlin", "Dilip", 1)
        val errorMessage = "Unexpected error occurred"

        every { courseServiceMock.addCourse(any())} throws RuntimeException(errorMessage)

        val response = webTestClient.post()
            .uri("/v1/courses")
            .bodyValue(courseDTO)
            .exchange()
            .expectStatus().is5xxServerError
            .expectBody(String::class.java)
            .returnResult()
            .responseBody

        assertEquals(errorMessage, response)
    }

    @Test
    fun retrieveAllCourse() {

        every { courseServiceMock.retrieveAllCourses(any())}.returnsMany(
            listOf(courseDTO(id = 1), courseDTO(id = 2, "Build Reactive Microservices using Spring WebFlux/SpringBoot"), courseDTO(id = 3, name="Wiremock for Java Developers")),
        )
        val courseDTOs = webTestClient.get()
            .uri("/v1/courses")
            .exchange()
            .expectStatus().isOk
            .expectBodyList(CourseDTO::class.java)
            .returnResult()
            .responseBody

        assertEquals(3, courseDTOs!!.size)
    }

    @Test
    fun updateCourse() {

        val oldCourse : Course = Course(null, "Build Restful APIs Using Spring Boot and Kotlin", "Dilip")

        val newName = "Test Restful Services in Kotlin"
        val newCategory = "TESTING"
        val newCourseDTO = CourseDTO(null, newName, newCategory)

        every {courseServiceMock.updateCourse(any(), any())} returns courseDTO(100, newName, newCategory)

        val updatedCourseDTO = webTestClient.put()
            .uri("/v1/courses/100")
            .bodyValue(newCourseDTO)
            .exchange()
            .expectStatus().is2xxSuccessful
            .expectBody(CourseDTO::class.java)
            .returnResult()
            .responseBody

        assertEquals(newCategory, updatedCourseDTO!!.category)
        assertEquals(newName, updatedCourseDTO!!.name)
    }

    @Test
    fun deleteCourse() {

        every {courseServiceMock.deleteCourse(any())} just runs

        val updatedCourseDTO = webTestClient.delete()
            .uri("/v1/courses/100")
            .exchange()
            .expectStatus().isNoContent
    }
}