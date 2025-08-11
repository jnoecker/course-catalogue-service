package dev.ambon.course_catalogue_service.controller

import PostgreSQLContainerInitializer
import dev.ambon.course_catalogue_service.dto.CourseDTO
import dev.ambon.course_catalogue_service.dto.InstructorDTO
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
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.web.util.UriComponentsBuilder

private val logger = KotlinLogging.logger {}

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@AutoConfigureWebTestClient
@AutoConfigureTestDatabase(replace= AutoConfigureTestDatabase.Replace.NONE)
class InstructorControllerIntegrationTest : PostgreSQLContainerInitializer() {

    @Autowired
    lateinit var webTestClient: WebTestClient

    @Autowired
    lateinit var instructorRepository: InstructorRepository

    @BeforeEach
    fun setUp() {
        instructorRepository.deleteAll()
        val instructor = instructorEntity()
        instructorRepository.save(instructor)
    }

    @Test
    fun addCourse() {
        val instructor = instructorEntity()
        val savedInstructorDTO = webTestClient.post()
            .uri("/v1/instructors")
            .bodyValue(instructor)
            .exchange()
            .expectStatus().isCreated
            .expectBody(InstructorDTO::class.java)
            .returnResult()
            .responseBody
        assertTrue {
            savedInstructorDTO!!.id != null
        }
        assertTrue {
            savedInstructorDTO!!.name == instructor.name
        }
    }
}