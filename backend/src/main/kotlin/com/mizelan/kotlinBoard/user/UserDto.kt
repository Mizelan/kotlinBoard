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
        val userId: String,
        @get:NotBlank
        val passWd: String,
        @get:NotBlank
        val confirmPassWd: String
)