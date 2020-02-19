package com.mizelan.kotlinBoard.security.jwt

import com.mizelan.kotlinBoard.utils.toDateTime
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import javassist.bytecode.ByteArray
import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.stereotype.Service
import java.nio.charset.StandardCharsets
import java.security.Key
import java.time.LocalDateTime
import java.util.*
import javax.crypto.SecretKey
import javax.servlet.http.HttpServletRequest

@Service
class JwtProviderImpl(
        @Value("\${security.token-expire-minute}")
        val tokenExpireMinute: Long = 30
) : JwtProvider {

    private val logger = KotlinLogging.logger {}

    companion object {
        private val secretKey: SecretKey = Keys.secretKeyFor(SignatureAlgorithm.HS512)
    }

    override fun generateToken(authentication: Authentication): String {

        logger.info { "Jwt 토큰 발급: ${authentication.name}"}

        val authorities = authentication.authorities.joinToString(",")
        return Jwts.builder()
                .setSubject(authentication.name)
                .claim("authorities", authorities)
                .setIssuedAt(Date())
                .signWith(secretKey, SignatureAlgorithm.HS512)
                .setExpiration(LocalDateTime.now().plusMinutes(tokenExpireMinute!!).toDateTime())
                .compact()
    }
    
    override fun getAuthentication(jws: String): Authentication? {
        try {
            logger.info { "Jwt 인증 시도: ${jws.substring(0, 10)}..."}

            if (jws.isEmpty())
                return null;

            val jwtsParser = Jwts.parserBuilder().setSigningKey(secretKey).build();
            val claims = jwtsParser.parseClaimsJws(jws).body ?: return null;
            if (claims.expiration.before(Date()))
                return null;

            val principal = claims.subject
            val credentials = null
            val authorities = claims["authorities"].toString().split(',')
                    .map { SimpleGrantedAuthority(it) }

            return UsernamePasswordAuthenticationToken(principal, credentials, authorities)
        } catch (e: Exception) { // TODO: 예외 구체화 하기
            logger.warn { e.toString() }
            return null;
        }
         
    }
}