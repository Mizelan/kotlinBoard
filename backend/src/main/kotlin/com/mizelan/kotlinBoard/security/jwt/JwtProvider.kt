package com.mizelan.kotlinBoard.security.jwt

import org.springframework.security.core.Authentication
import java.net.PasswordAuthentication
import javax.servlet.http.HttpServletRequest

interface JwtProvider {
    fun generateToken(authentication: Authentication): String
    fun getAuthentication(jws: String): Authentication?
}