package com.mizelan.kotlinBoard.user

import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository : JpaRepository<UserEntity, String> {
    fun findByUsername(userId: String): UserEntity?
}