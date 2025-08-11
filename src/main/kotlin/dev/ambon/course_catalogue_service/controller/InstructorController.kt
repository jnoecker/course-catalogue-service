package dev.ambon.course_catalogue_service.controller

import dev.ambon.course_catalogue_service.dto.InstructorDTO
import dev.ambon.course_catalogue_service.service.InstructorService
import org.springframework.http.HttpStatus
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

/**
 * REST controller for Instructor resources.
 *
 * Endpoints:
 * - POST /v1/instructors: Create a new instructor
 */
@RestController
@RequestMapping("/v1/instructors")
@Validated
class InstructorController(val instructorService: InstructorService, ) {

    /**
     * Create a new Instructor.
     * Returns 201 Created with the persisted Instructor payload.
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createInstructor(@RequestBody @Validated instructorDTO: InstructorDTO) = instructorService.createInstructor(instructorDTO)
}