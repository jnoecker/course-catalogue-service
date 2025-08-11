package dev.ambon.course_catalogue_service.repository

import dev.ambon.course_catalogue_service.entity.Instructor
import org.springframework.data.repository.CrudRepository

interface InstructorRepository : CrudRepository<Instructor, Int> {
}