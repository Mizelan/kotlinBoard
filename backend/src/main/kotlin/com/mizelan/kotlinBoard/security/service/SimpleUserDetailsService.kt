package com.mizelan.kotlinBoard.security.service

import com.mizelan.kotlinBoard.user.AppUser
import com.mizelan.kotlinBoard.user.UserEntity
import com.mizelan.kotlinBoard.user.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Component

@Component
class SimpleUserDetailsService (
        @Autowired
        private val userRepository: UserRepository
) : UserDetailsService {
    override fun loadUserByUsername(username: String): AppUser {
        val userEntity = userRepository.findByUsername(username)
                ?: throw UsernameNotFoundException("not found user: $username")

        return AppUser(
                userEntity.id!!,
                userEntity.username,
                userEntity.password,
                userEntity.authorities.split(",").map {
                    SimpleGrantedAuthority(it)
                }.toList())
    }
}
