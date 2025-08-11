package dev.ambon.course_catalogue_service.dto

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull

data class CourseDTO(
    val id : Int?,
    @get:NotBlank(message = "Course name must not be blank")
    var name : String,
    @get:NotBlank(message = "Course category must not be blank")
    var category : String,
    @get:NotNull(message = "Instructor id must not be null")
    val instructorId : Int? = null
)