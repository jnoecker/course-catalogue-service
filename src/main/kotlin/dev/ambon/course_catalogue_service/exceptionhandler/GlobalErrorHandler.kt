package dev.ambon.course_catalogue_service.exceptionhandler

import dev.ambon.course_catalogue_service.exception.InstructorNotValidException
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatusCode
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.context.request.WebRequest
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler

private val logger = KotlinLogging.logger {}

/**
 * Global exception handling for REST controllers.
 *
 * Converts validation and domain exceptions into meaningful HTTP responses
 * with appropriate status codes and messages.
 */
@Component
@ControllerAdvice
class GlobalErrorHandler : ResponseEntityExceptionHandler() {

    /**
     * Handles bean validation errors (e.g., @Valid/@Validated annotated payloads).
     * Returns 400 with a comma-separated list of error messages.
     */
    override fun handleMethodArgumentNotValid(
        ex: MethodArgumentNotValidException,
        headers: HttpHeaders,
        status: HttpStatusCode,
        request: WebRequest
    ): ResponseEntity<in Any>? {
        logger.error("MethodArgumentNotValidException observed: ${ex.message}", ex)
        val errors = ex.bindingResult.allErrors.map { error -> error.defaultMessage!! }.sorted()
        logger.error("The following errors were found: $errors", ex)
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors.joinToString ( ", ") { it })
    }

    /**
     * Maps InstructorNotValidException to 400 Bad Request.
     */
    @ExceptionHandler(InstructorNotValidException::class)
    fun handleAllExceptions(ex: InstructorNotValidException, request: WebRequest) : ResponseEntity<Any> {
        logger.error("InstructorNotValidException observed: ${ex.message}", ex)
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.message)
    }

    /**
     * Fallback for unexpected exceptions, returning 500 Internal Server Error.
     */
    @ExceptionHandler(Exception::class)
    fun handleAllExceptions(ex: Exception, request: WebRequest) : ResponseEntity<Any> {
        logger.error("Exception observed: ${ex.message}", ex)
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.message)
    }
}