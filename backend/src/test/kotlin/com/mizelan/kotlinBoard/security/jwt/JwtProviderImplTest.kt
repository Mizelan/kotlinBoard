package com.mizelan.kotlinBoard.security.jwt

import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.TestPropertySource

class JwtProviderImplTest {
    @Test
    fun generateTokenAndAuthentication() {
        val jwtProvider = JwtProviderImpl(tokenExpireMinute = 1)
        val authentication: Authentication = mock(Authentication::class.java)
        `when`(authentication.name).thenReturn("name")
        `when`(authentication.authorities).thenReturn(
                mutableListOf(
                        SimpleGrantedAuthority("ADMIN"),
                        SimpleGrantedAuthority("USER")))

        val token = jwtProvider.generateToken(authentication)
        Assertions.assertNotNull(token)

        val actualAuthentication = jwtProvider.getAuthentication(token)!!
        assertEquals("name", actualAuthentication.name)
        assertEquals(2, actualAuthentication.authorities.count())
        assertEquals("ADMIN", actualAuthentication.authorities.elementAt(0).authority)
        assertEquals("USER", actualAuthentication.authorities.elementAt(1).authority)

        // jwtProvider 다른 인스턴스에도 같은 결과가 나온다.
        val anotherJwtProvider = JwtProviderImpl(tokenExpireMinute = 1)
        val anotheractualAuthentication = anotherJwtProvider.getAuthentication(token)!!
        assertEquals("name", anotheractualAuthentication.name)
    }
}