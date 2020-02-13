package com.mizelan.kotlinBoard.user

import com.fasterxml.jackson.annotation.JsonFormat
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.time.LocalDateTime
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

// TODO: seq id 추가하기
@Entity
@Table(name = "user")
data class UserEntity(
    @Id
    @Column(nullable = false)
    val userId: String,
    @Column(nullable = false)
    val passWd: String,
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    val createdAt: LocalDateTime? = null,
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @UpdateTimestamp
    @Column(nullable = false)
    val updatedAt: LocalDateTime? = null
)