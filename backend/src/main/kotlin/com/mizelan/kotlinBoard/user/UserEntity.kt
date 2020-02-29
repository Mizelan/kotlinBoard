package com.mizelan.kotlinBoard.user

import com.fasterxml.jackson.annotation.JsonFormat
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.time.LocalDateTime
import javax.persistence.*

@Entity
@Table(name = "user")
data class UserEntity(
        @Id @Column
        @GeneratedValue
        val id: Long? = null,

        @Column(nullable = false)
        val username: String,

        @Column(nullable = false)
        val password: String,

        @Column
        val authorities: String,

        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        @CreationTimestamp
        @Column(nullable = false, updatable = false)
        val createdAt: LocalDateTime? = null,

        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        @UpdateTimestamp
        @Column(nullable = false)
        val updatedAt: LocalDateTime? = null
)