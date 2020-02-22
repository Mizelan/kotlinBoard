package com.mizelan.kotlinBoard.security.jwt

import io.jsonwebtoken.ExpiredJwtException
import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service
import org.springframework.web.filter.GenericFilterBean
import javax.servlet.FilterChain
import javax.servlet.ServletRequest
import javax.servlet.ServletResponse
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Service
class JwtAuthenticationFilter(
        @Autowired val jwtProvider: JwtProvider
) : GenericFilterBean() {

    override fun doFilter(request: ServletRequest, response: ServletResponse, filterChain: FilterChain) {
        if (request !is HttpServletRequest) return;
        if (response !is HttpServletResponse) return;

        val authHeader = request.getHeader("Authorization") ?: ""
        if (authHeader.contains("Bearer")) {
            val jws = authHeader.replace("Bearer", "").trim()
            try {
                val authentication = jwtProvider.getAuthentication(jws)
                if (authentication != null) {
                    SecurityContextHolder.getContext().authentication = authentication
                }
            } catch (e: Exception) {
                logger.warn { "Jwt 인증 오류: $e" }
                SecurityContextHolder.clearContext()
            }
        }

        filterChain.doFilter(request, response)
    }

}