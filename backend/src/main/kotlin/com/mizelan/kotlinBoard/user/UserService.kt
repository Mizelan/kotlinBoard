package com.mizelan.kotlinBoard.user

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
class UserService {
    @Autowired
    private lateinit var  simpleUserDetailsService: SimpleUserDetailsService
    @Autowired
    private lateinit var  userAuthRepository: UserAuthRepository
    @Autowired
    private lateinit var  userRepository: UserRepository
    @Autowired
    private lateinit var  passwordEncoder: PasswordEncoder
    @Autowired
    private lateinit var jwtProvider: JwtProvider

    fun getUserDetails(userId: String, passWd: String): UserDetails {
        val userInfo = simpleUserDetailsService.loadUserByUsername(userId)
        if (!passwordEncoder.matches(passWd, userInfo.password))
            throw BadCredentialsException("invalid password")

        return userInfo
    }

    fun generatorToken(details: UserDetails) =
        jwtProvider.generateToken(UsernamePasswordAuthenticationToken(details, details.password, details.authorities))

    fun preCreateUser(userId: String, passWd: String, confirmPassWd: String) {
        val dbInfo = userRepository.findByUserId(userId)
        if (dbInfo != null)
            throw IllegalArgumentException("already registered user.")

        if (passWd != confirmPassWd)
            throw IllegalArgumentException("invalid password")
    }

    @Transactional
    fun createUser(userId: String, passWd: String) {
        userRepository.save(
                UserEntity(userId, passwordEncoder.encode(passWd)))
        userAuthRepository.save(
                UserAuthEntity(
                        userId = userId,
                        authorityCd = arrayOf(
                                UserRole.USER.name,
                                UserRole.ADMIN.name
                        ).joinToString(",")))
    }
}
