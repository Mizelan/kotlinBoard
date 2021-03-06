package com.mizelan.kotlinBoard.post

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonGetter
import com.fasterxml.jackson.annotation.JsonIgnore
import com.mizelan.kotlinBoard.user.UserEntity
import org.hibernate.annotations.Formula
import org.hibernate.annotations.JoinFormula
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "`post`")
data class PostEntity(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        val id: Long? = null,

        @JsonIgnore
        @OneToOne(fetch = FetchType.LAZY) @JoinColumn(name = "user_id")
        val author: UserEntity?,
        val title: String,
        val content: String?,
        val viewCount: Long = 0,
        val createdAt: Date = Date(),
        val updatedAt: Date = Date()) {

        @JsonGetter
        fun authorId() = author?.id

        @JsonGetter
        fun authorName() = author?.username
}