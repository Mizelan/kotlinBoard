package com.mizelan.kotlinBoard.post

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseBody
import java.net.BindException

@ControllerAdvice(assignableTypes = [PostController::class])
class PostControllerAdvice {

    @ExceptionHandler//(value = [BindException::class, MethodArgumentNotValidException::class])
    @ResponseBody
    fun handleMethodArgumentNotValidException(ex: Exception): ResponseEntity<String> {

        var errorMessage: String? = null
        if (ex is MethodArgumentNotValidException) {
            errorMessage = ex.toString()
        } else {
            errorMessage = ex.toString()
        }

        return ResponseEntity(errorMessage, HttpStatus.BAD_REQUEST)
    }
}