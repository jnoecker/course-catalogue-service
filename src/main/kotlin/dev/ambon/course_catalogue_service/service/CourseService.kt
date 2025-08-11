package dev.ambon.course_catalogue_service.service

import dev.ambon.course_catalogue_service.dto.CourseDTO
import dev.ambon.course_catalogue_service.entity.Course
import dev.ambon.course_catalogue_service.exception.CourseNotFoundException
import dev.ambon.course_catalogue_service.exception.InstructorNotValidException
import dev.ambon.course_catalogue_service.repository.CourseRepository
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.stereotype.Service

private val logger = KotlinLogging.logger {}

@Service
class CourseService(val courseRepository: CourseRepository, val instructorService: InstructorService)  {
    fun addCourse(courseDTO: CourseDTO) : CourseDTO {

        val instructorOptional = instructorService.findByInstructorId(courseDTO.instructorId!!)

        if(!instructorOptional.isPresent) {
            throw InstructorNotValidException("Instructor Not valid: ${courseDTO.instructorId}")
        }

        val courseEntity = courseDTO.let {
            Course(null, it.name, it.category, instructorOptional.get())
        }

        courseRepository.save(courseEntity)

        logger.info{"Saved course is $courseEntity"}

        return courseEntity.let {
            CourseDTO(it.id, it.name, it.category, it.instructor!!.id)
        }

    }

    fun retrieveAllCourses(courseName: String?): List<CourseDTO> {
        val courses = courseName?.let {
            courseRepository.findByNameContainingIgnoreCase(courseName)
        } ?: courseRepository.findAll()
        return courses
            .map {
                CourseDTO(it.id, it.name, it.category)
            }
    }

    fun updateCourse(courseId: Int, courseDTO: CourseDTO): CourseDTO {
        val existingCourse = courseRepository.findById(courseId)
        return if(existingCourse.isPresent) {
            existingCourse.get().let {
                it.name = courseDTO.name
                it.category = courseDTO.category
                courseRepository.save(it)
                CourseDTO(it.id, it.name, it.category)
            }
        } else {
            // throw an exception
            throw CourseNotFoundException("No course with id $courseId found")
        }
    }

    fun deleteCourse(courseId: Int) {
        val existingCourse = courseRepository.findById(courseId)

        return if(existingCourse.isPresent) {
            logger.info{"Course with id $courseId deleted"}
            courseRepository.deleteById(courseId)
        } else {
            throw CourseNotFoundException("No course with id $courseId found")
        }
    }
}
