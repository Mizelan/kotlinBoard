package com.mizelan.kotlinBoard.user

import org.springframework.data.jpa.repository.JpaRepository

interface UserAuthRepository : JpaRepository<UserAuthEntity, Long> {
    fun findByUserId(userId: String): List<UserAuthEntity>?
}
