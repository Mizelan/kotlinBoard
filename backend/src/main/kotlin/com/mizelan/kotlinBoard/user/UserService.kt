package com.mizelan.kotlinBoard.user

import com.mizelan.kotlinBoard.exception.AlreadyRegisteredUsernameException
import com.mizelan.kotlinBoard.exception.ConfirmPasswordNotMatchedException
import com.mizelan.kotlinBoard.security.jwt.JwtProvider
import com.mizelan.kotlinBoard.security.service.SimpleUserDetailsService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import javax.transaction.Transactional

@Service
class UserService(
        @Autowired private val simpleUserDetailsService: SimpleUserDetailsService,
        @Autowired private val userRepository: UserRepository,
        @Autowired private val passwordEncoder: PasswordEncoder,
        @Autowired private val jwtProvider: JwtProvider
) {
    fun getUserDetails(username: String, password: String): AppUser {
        val userInfo = simpleUserDetailsService.loadUserByUsername(username)
        if (!passwordEncoder.matches(password, userInfo.password))
            throw BadCredentialsException("invalid password")

        return userInfo
    }

    fun generatorToken(details: UserDetails) =
        jwtProvider.generateToken(UsernamePasswordAuthenticationToken(details, details.password, details.authorities))

    fun validateCreateUserRequest(userId: String, passWd: String, confirmPassWd: String) {
        val dbInfo = userRepository.findByUsername(userId)
        if (dbInfo != null)
            throw AlreadyRegisteredUsernameException()

        if (passWd != confirmPassWd)
            throw ConfirmPasswordNotMatchedException()
    }

    @Transactional
    fun createUser(username: String, password: String) {
        val authorities = arrayOf(
                UserRole.USER.name,
                UserRole.ADMIN.name
        ).joinToString(",")

        userRepository.save(
                UserEntity(
                        username = username,
                        password = passwordEncoder.encode(password),
                        authorities = authorities))
    }

    @Transactional
    fun getUser(username: String) =
        userRepository.findByUsername(username)
}
