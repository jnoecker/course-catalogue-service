package dev.ambon.course_catalogue_service.util

import dev.ambon.course_catalogue_service.dto.CourseDTO
import dev.ambon.course_catalogue_service.entity.Course
import dev.ambon.course_catalogue_service.entity.Instructor

//import dev.ambon.course_catalogue_service.entity.Instructor

/*fun courseDTO(
    id: Int? = null,
    name: String = "Build RestFul APis using Spring Boot and Kotlin",
    category: String = "Dilip Sundarraj",
) = CourseDTO(
    id,
    name,
    category
)*/


fun courseDTO(
    id: Int? = null,
    name: String = "Build Restful APIs Using Spring Boot and Kotlin",
    category: String = "Dilip",
//    instructorId: Int? = 1
) = CourseDTO(
    id,
    name,
    category,
//    instructorId
)

fun courseEntityList(instructor: Instructor? = null) = listOf(
    Course(null,
        "Build RestFul APis using SpringBoot and Kotlin", "Development",
        instructor),
    Course(null,
        "Build Reactive Microservices using Spring WebFlux/SpringBoot", "Development"
        ,instructor
    ),
    Course(null,
        "Wiremock for Java Developers", "Development" ,
        instructor)
)
//
fun instructorEntity(name : String = "Dilip Sundarraj")
        = Instructor(null, name)





