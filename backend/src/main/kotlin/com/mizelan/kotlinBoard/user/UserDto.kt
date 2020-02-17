package com.mizelan.kotlinBoard.user

import javax.validation.constraints.NotBlank

data class LoginRequest(
    val username: @NotBlank String,
    val password: @NotBlank String
)

data class SignUpRequest(
    val userId: @NotBlank String,
    val passWd: @NotBlank String,
    val confirmPassWd: @NotBlank String
)