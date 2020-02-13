package com.mizelan.kotlinBoard.user

import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository : JpaRepository<UserEntity, String> {
    fun findByUserId(userId: String): UserEntity?
}