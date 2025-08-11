package dev.ambon.course_catalogue_service.controller

import dev.ambon.course_catalogue_service.dto.CourseDTO
import dev.ambon.course_catalogue_service.service.CourseService
import org.springframework.http.HttpStatus
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

/**
 * REST controller exposing CRUD operations for Course resources.
 *
 * Endpoints:
 * - POST /v1/courses: Create a new course (requires a valid instructorId)
 * - GET /v1/courses: List all courses or filter by course_name query parameter
 * - PUT /v1/courses/{course_id}: Update an existing course's name/category
 * - DELETE /v1/courses/{course_id}: Delete a course
 */
@RestController
@RequestMapping("/v1/courses")
@Validated
class CourseController(val courseService : CourseService) {

    /**
     * Create a new Course.
     * Returns 201 Created with the persisted Course payload.
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun addCourse(@RequestBody @Validated courseDTO: CourseDTO) : CourseDTO {
        return courseService.addCourse(courseDTO)
    }

    /**
     * Retrieve all courses, optionally filtering by name using the
     * `course_name` query parameter (case-insensitive contains).
     */
    @GetMapping
    fun retrieveAllCourses (@RequestParam("course_name", required = false) courseName: String?) : List<CourseDTO> = courseService.retrieveAllCourses(courseName)

    /**
     * Update the name/category of the course identified by course_id.
     * Throws 404 if not found.
     */
    @PutMapping("/{course_id}")
    fun updateCourse(@RequestBody courseDTO: CourseDTO, @PathVariable("course_id") courseId: Int) = courseService.updateCourse(courseId, courseDTO)

    /**
     * Delete the course identified by course_id.
     * Returns 204 No Content on success, or 404 if not found.
     */
    @DeleteMapping("/{course_id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteCourse(@PathVariable("course_id") courseId: Int) = courseService.deleteCourse(courseId)

}