package com.mizelan.kotlinBoard.user

import javax.validation.constraints.NotBlank

data class LoginRequest(
        @get:NotBlank
        val username: String,
        @get:NotBlank
        val password: String
)

data class SignUpRequest(
        @get:NotBlank
        val username: String,
        @get:NotBlank
        val password: String,
        @get:NotBlank
        val confirmPassword: String
)