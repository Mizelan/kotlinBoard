package com.mizelan.kotlinBoard.user

import com.fasterxml.jackson.annotation.JsonFormat
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.time.LocalDateTime
import javax.persistence.*

@Entity
@Table(name = "user_auth")
data class UserAuthEntity (
    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.AUTO)
    val userAuthId: Long? = null,
    @Column(nullable = false)
    val userId: String,
    @Column(nullable = false)
    val authorityCd: String,
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    val createdAt: LocalDateTime? = null,
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @UpdateTimestamp
    @Column(nullable = false)
    val updatedAt: LocalDateTime? = null
)
