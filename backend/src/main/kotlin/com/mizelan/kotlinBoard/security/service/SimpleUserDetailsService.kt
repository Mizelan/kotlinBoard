package com.mizelan.kotlinBoard.security.service

import com.mizelan.kotlinBoard.user.UserAuthEntity
import com.mizelan.kotlinBoard.user.UserAuthRepository
import com.mizelan.kotlinBoard.user.UserEntity
import com.mizelan.kotlinBoard.user.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Component
import java.util.stream.Collectors

@Component
class SimpleUserDetailsService : UserDetailsService {
    @Autowired
    private lateinit var userAuthRepository: UserAuthRepository
    @Autowired
    private lateinit var userRepository: UserRepository

    override fun loadUserByUsername(userId: String): User {
        val dbUserInfo: UserEntity = userRepository.findByUserId(userId)
                ?: throw UsernameNotFoundException("not found user: $userId")

        val authList = userAuthRepository.findByUserId(userId)
        if (authList.isNullOrEmpty())
            throw UsernameNotFoundException("insufficient permission.")

        return User(
                dbUserInfo.userId,
                dbUserInfo.passWd,
                authList.map { SimpleGrantedAuthority(it.authorityCd) }.toList())
    }
}
