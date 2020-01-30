package com.mizelan.kotlinBoard.post

import java.util.*
import javax.persistence.*

@Entity
@Table(name = "`post`")
data class Post(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        val id: Long? = null,
        val title: String,
        val content: String?,
        val createdAt: Date = Date(),
        val updatedAt: Date = Date())