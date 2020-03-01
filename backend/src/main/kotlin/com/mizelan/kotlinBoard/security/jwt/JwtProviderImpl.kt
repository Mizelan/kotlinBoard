package com.mizelan.kotlinBoard.security.jwt

import com.mizelan.kotlinBoard.user.AppUser
import com.mizelan.kotlinBoard.utils.toDateTime
import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.util.*
import java.util.Base64.getEncoder
import javax.annotation.PostConstruct
import javax.crypto.SecretKey
import javax.crypto.spec.SecretKeySpec


@Service
class JwtProviderImpl(
        @Value("\${security.token-expire-minute}")
        private val tokenExpireMinute: Long = 30,

        @Value("\${security.key}")
        private var secretKey: String? = null
) : JwtProvider {
    private val logger = KotlinLogging.logger {}

    init {
        secretKey = getEncoder().encodeToString(secretKey!!.toByteArray())
    }

    override fun generateToken(authentication: Authentication): String {

        logger.debug { "Jwt 토큰 발급 시도: ${authentication.name}"}

        val appUser = authentication.principal as AppUser;

        try {
            val authorities = authentication.authorities.joinToString(",")
            return Jwts.builder()
                    .setSubject(authentication.name)
                    .claim("authorities", authorities)
                    .claim("userId", appUser.userId)
                    .setIssuedAt(Date())
                    .signWith(SignatureAlgorithm.HS512, secretKey)
                    .setExpiration(LocalDateTime.now().plusMinutes(tokenExpireMinute).toDateTime())
                    .compact()
        } catch (e: Exception) {
            logger.warn { "Jwt 토큰 발급 오류: $e" }
            throw e
        }
    }

    override fun getAuthentication(jws: String): Authentication? {
        try {
            logger.debug { "Jwt 인증 검사 시작: ${jws.substring(0, 10)}..." }

            val jwtsParser = Jwts.parserBuilder().setSigningKey(secretKey).build();
            val claims = jwtsParser.parseClaimsJws(jws).body ?: return null;
            val principal = claims.subject
            val credentials = null
            val authorities = claims["authorities"].toString().split(',')
                    .map { SimpleGrantedAuthority(it) }
            logger.debug { "Jwt authorities: (${authorities.joinToString(",")}) ${jws.substring(0, 10)}..." }

            val rv = UsernamePasswordAuthenticationToken(principal, credentials, authorities)
            logger.info { "Jwt 인증 성공: ${jws.substring(0, 10)}..." }
            return rv;
        } catch (e: ExpiredJwtException) {
            logger.debug { "Jwt 유효시간 만료됨: ${jws.substring(0, 10)}..." }
            throw e
        }
    }
}