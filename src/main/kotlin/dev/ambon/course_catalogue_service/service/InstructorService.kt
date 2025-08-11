package dev.ambon.course_catalogue_service.service

import dev.ambon.course_catalogue_service.dto.InstructorDTO
import dev.ambon.course_catalogue_service.entity.Instructor
import dev.ambon.course_catalogue_service.repository.InstructorRepository
import org.springframework.stereotype.Service
import java.util.Optional

/**
 * Business logic for managing Instructor entities.
 */
@Service
class InstructorService(val instructorRepository: InstructorRepository) {

    /**
     * Persist a new Instructor and return the saved DTO.
     */
    fun createInstructor(instructorDTO: InstructorDTO): InstructorDTO {
        val instructorEntity = instructorDTO.let {
            Instructor(it.id, it.name)
        }
        instructorRepository.save(instructorEntity)

        return instructorEntity.let {
            InstructorDTO(it.id, it.name)
        }
    }

    /**
     * Locate an Instructor by id.
     */
    fun findByInstructorId(instructorId: Int) : Optional<Instructor> {
        return instructorRepository.findById(instructorId)
    }

}
