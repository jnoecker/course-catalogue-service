package dev.ambon.course_catalogue_service.controller

import com.ninjasquad.springmockk.MockkBean
import dev.ambon.course_catalogue_service.dto.InstructorDTO
import dev.ambon.course_catalogue_service.service.InstructorService
import io.mockk.every
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.reactive.server.WebTestClient

@WebMvcTest(controllers = [InstructorController::class])
@ActiveProfiles("test")
@AutoConfigureWebTestClient
class InstructorControllerUnitTest {
    @Autowired
    lateinit var webTestClient: WebTestClient

    @MockkBean
    lateinit var instructorServiceMock: InstructorService

    @Test
    fun addInstructor() {
        val instructorDTO = InstructorDTO(null, "John Noecker")

        every { instructorServiceMock.createInstructor(any()) } returns InstructorDTO(1, instructorDTO.name)

        val savedInstructorDTO = webTestClient.post()
            .uri("/v1/instructors")
            .bodyValue(instructorDTO)
            .exchange()
            .expectStatus().isCreated
            .expectBody(InstructorDTO::class.java)
            .returnResult()
            .responseBody
        assertTrue {
            savedInstructorDTO!!.id == 1
        }
        assertTrue {
            savedInstructorDTO!!.name == instructorDTO.name
        }
    }

    @Test
    fun addInstructor_validation() {
        val instructorDTO = InstructorDTO(null, "")

        every { instructorServiceMock.createInstructor(any()) } returns InstructorDTO(1, "")

        val response = webTestClient.post()
            .uri("/v1/instructors")
            .bodyValue(instructorDTO)
            .exchange()
            .expectStatus().isBadRequest
            .expectBody(String::class.java)
            .returnResult()
            .responseBody

        assertEquals("Instructor name must not be blank", response)
    }
}