package com.mizelan.kotlinBoard

import mu.KotlinLogging
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseBody
import java.net.BindException

@ControllerAdvice//(assignableTypes = [PostController::class])
class GlobalControllerAdvice {

    val logger = KotlinLogging.logger {}

    @ExceptionHandler//(value = [BindException::class, MethodArgumentNotValidException::class])
    @ResponseBody
    fun handleMethodArgumentNotValidException(ex: Exception): ResponseEntity<String> {
        if (ex is MethodArgumentNotValidException) {
            return ResponseEntity(ex.toString(), HttpStatus.BAD_REQUEST)
        }

        val errorMessage = ex.toString()
        logger.error(errorMessage)
        return ResponseEntity(errorMessage, HttpStatus.INTERNAL_SERVER_ERROR)
    }
}