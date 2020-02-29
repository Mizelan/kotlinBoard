package com.mizelan.kotlinBoard.security.service

import com.mizelan.kotlinBoard.user.UserEntity
import com.mizelan.kotlinBoard.user.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Component

@Component
class SimpleUserDetailsService (
        @Autowired
        private val userRepository: UserRepository
) : UserDetailsService {
    override fun loadUserByUsername(userId: String): User {
        val dbUserInfo: UserEntity = userRepository.findByUsername(userId)
                ?: throw UsernameNotFoundException("not found user: $userId")

        return User(
                dbUserInfo.username,
                dbUserInfo.password,
                dbUserInfo.authorities.split(",").map {
                    SimpleGrantedAuthority(it)
                }.toList())
    }
}
