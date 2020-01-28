package com.mizelan.kotlinBoard.post

data class CreatePostRequest(val title: String, val content: String?)
data class UpdatePostRequest(val title: String, val content: String?)
