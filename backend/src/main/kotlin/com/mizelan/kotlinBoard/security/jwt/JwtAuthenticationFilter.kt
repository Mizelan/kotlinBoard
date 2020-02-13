package com.mizelan.kotlinBoard.security.jwt

import io.jsonwebtoken.ExpiredJwtException
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.filter.GenericFilterBean
import java.io.IOException
import javax.servlet.FilterChain
import javax.servlet.ServletException
import javax.servlet.ServletRequest
import javax.servlet.ServletResponse
import javax.servlet.http.HttpServletRequest

class JwtAuthenticationFilter(private val jwtProvider: JwtProvider) : GenericFilterBean() {
    override fun doFilter(
            servletRequest: ServletRequest, 
            servletResponse: ServletResponse, 
            filterChain: FilterChain) {
        
        try {
            val authentication = jwtProvider.getAuthentication(servletRequest as HttpServletRequest)
            if (authentication != null) {
                SecurityContextHolder.getContext().authentication = authentication
            }
        } catch (e: ExpiredJwtException) {
            // TODO: 에러 로그 추가하기
            SecurityContextHolder.clearContext()
        }
        filterChain.doFilter(servletRequest, servletResponse)
    }

}