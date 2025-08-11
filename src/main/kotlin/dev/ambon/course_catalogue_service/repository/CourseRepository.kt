package dev.ambon.course_catalogue_service.repository

import dev.ambon.course_catalogue_service.entity.Course
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository

interface CourseRepository : CrudRepository<Course, Int> {
    fun findByNameContainingIgnoreCase(courseName: String) : List<Course>

    @Query(value = "SELECT * FROM courses WHERE name like %?1%", nativeQuery = true)
    fun findCoursesByName(courseName: String) : List<Course>
}