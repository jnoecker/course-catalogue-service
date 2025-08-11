package dev.ambon.course_catalogue_service.controller

import PostgreSQLContainerInitializer
import dev.ambon.course_catalogue_service.dto.CourseDTO
import dev.ambon.course_catalogue_service.entity.Course
import dev.ambon.course_catalogue_service.repository.CourseRepository
import dev.ambon.course_catalogue_service.repository.InstructorRepository
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.reactive.server.WebTestClient
import dev.ambon.course_catalogue_service.util.courseEntityList
import dev.ambon.course_catalogue_service.util.instructorEntity
import io.github.oshai.kotlinlogging.KotlinLogging
import org.junit.jupiter.api.Assertions.assertEquals
import org.springframework.boot.autoconfigure.data.RepositoryType
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.springframework.web.util.UriComponentsBuilder
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import org.testcontainers.utility.DockerImageName

private val logger = KotlinLogging.logger {}

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@AutoConfigureWebTestClient
@AutoConfigureTestDatabase(replace= AutoConfigureTestDatabase.Replace.NONE)
class CourseControllerIntegrationTest : PostgreSQLContainerInitializer() {

    @Autowired
    lateinit var webTestClient: WebTestClient

    @Autowired
    lateinit var courseRepository: CourseRepository

    @Autowired
    lateinit var instructorRepository : InstructorRepository

    @BeforeEach
    fun setUp() {
        courseRepository.deleteAll()
        instructorRepository.deleteAll()

        val instructor = instructorEntity()
        instructorRepository.save(instructor)

        val courses = courseEntityList(instructor)
        courseRepository.saveAll(courses)
    }

    @Test
    fun addCourse() {
        val instructor = instructorRepository.findAll().first()
        val courseDTO = CourseDTO(null, "Build Restful APIs Using Spring Boot and Kotlin", "Dilip", instructor.id)
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
    fun retrieveAllCourse() {
        val courseDTOs = webTestClient.get()
            .uri("/v1/courses")
            .exchange()
            .expectStatus().isOk
            .expectBodyList(CourseDTO::class.java)
            .returnResult()
            .responseBody

        logger.info{"Course DTOs: $courseDTOs"}
        assertEquals(3, courseDTOs!!.size)
    }

    @Test
    fun retrieveAllCoursesByName() {

        val uri = UriComponentsBuilder.fromUriString("/v1/courses").queryParam("course_name", "SpringBoot").toUriString()

        val courseDTOs = webTestClient.get()
            .uri(uri)
            .exchange()
            .expectStatus().isOk
            .expectBodyList(CourseDTO::class.java)
            .returnResult()
            .responseBody

        logger.info{"Course DTOs: $courseDTOs"}
        assertEquals(2, courseDTOs!!.size)
    }

    @Test
    fun updateCourse() {
        val instructor = instructorRepository.findAll().first()

        val oldCourse : Course = Course(null, "Build Restful APIs Using Spring Boot and Kotlin", "Dilip", instructor)
        courseRepository.save(oldCourse)


        val newName = "Test Restful Services in Kotlin"
        val newCategory = "TESTING"
        val newCourseDTO = CourseDTO(null, newName, newCategory, 1)

        val updatedCourseDTO = webTestClient.put()
            .uri("/v1/courses/${oldCourse.id}")
            .bodyValue(newCourseDTO)
            .exchange()
            .expectStatus().is2xxSuccessful
            .expectBody(CourseDTO::class.java)
            .returnResult()
            .responseBody

        logger.info{"Updated Course DTO: $updatedCourseDTO"}
        assertEquals(newCategory, updatedCourseDTO!!.category)
        assertEquals(newName, updatedCourseDTO!!.name)
    }

    @Test
    fun deleteCourse() {
        val instructor = instructorRepository.findAll().first()

        val oldCourse : Course = Course(null, "Build Restful APIs Using Spring Boot and Kotlin", "Dilip", instructor)
        courseRepository.save(oldCourse)


        val newName = "Test Restful Services in Kotlin"
        val newCategory = "TESTING"
        val newCourseDTO = CourseDTO(null, newName, newCategory)

        val updatedCourseDTO = webTestClient.delete()
            .uri("/v1/courses/${oldCourse.id}")
            .exchange()
            .expectStatus().isNoContent
    }

}