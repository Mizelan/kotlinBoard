package com.mizelan.kotlinBoard.post

import java.time.LocalDateTime
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "`post`")
class Post {


    constructor(title: String, content: String?, createdAt: Date = Date(), updatedAt: Date = Date())  {
        this.title = title
        this.content = content
        this.createdAt = createdAt
        this.updatedAt = updatedAt
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null
    var title: String
    var content: String? = null
    var createdAt: Date
    var updatedAt: Date

    fun copy(): Post = Post(title, content, createdAt, updatedAt)
}