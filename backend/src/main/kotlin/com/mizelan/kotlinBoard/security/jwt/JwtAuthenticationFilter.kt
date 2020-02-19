package com.mizelan.kotlinBoard.security.jwt

import io.jsonwebtoken.ExpiredJwtException
import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service
import org.springframework.web.filter.GenericFilterBean
import javax.servlet.FilterChain
import javax.servlet.ServletRequest
import javax.servlet.ServletResponse
import javax.servlet.http.HttpServletRequest

@Service
class JwtAuthenticationFilter(
        @Autowired val jwtProvider: JwtProvider
) : GenericFilterBean() {

    override fun doFilter(
            servletRequest: ServletRequest, 
            servletResponse: ServletResponse, 
            filterChain: FilterChain) {
        
        try {
            val request = servletRequest as HttpServletRequest
            val authHeader = request.getHeader("Authorization") ?: ""
            if (authHeader.contains("Bearer")) {
                val jws = authHeader.replace("Bearer", "").trim()
                val authentication = jwtProvider.getAuthentication(jws)
                if (authentication != null) {
                    SecurityContextHolder.getContext().authentication = authentication
                }
            }
        } catch (e: ExpiredJwtException) {
            logger.warn { "Jwt토큰 만료: ${e.toString()}" }
            SecurityContextHolder.clearContext()
        }
        filterChain.doFilter(servletRequest, servletResponse)
    }

}