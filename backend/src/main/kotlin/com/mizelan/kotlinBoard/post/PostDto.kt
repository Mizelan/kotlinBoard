package com.mizelan.kotlinBoard.post

import javax.validation.constraints.Min
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.NotNull

data class CreatePostRequest(
        @get:NotBlank
        val title: String,
        @get:NotBlank
        val content: String?)

data class UpdatePostRequest(
        @get:NotBlank
        val title: String,
        @get:NotBlank
        val content: String?)

data class PaginationInfo(
        val currentPage: Int,
        val maxPage: Int,
        val countOfPage: Int)