package com.mizelan.kotlinBoard.security.jwt

import com.mizelan.kotlinBoard.utils.toDateTime
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import java.time.LocalDateTime
import java.util.*
import javax.crypto.SecretKey
import javax.servlet.http.HttpServletRequest


class JwtProviderImpl : JwtProvider {

    @Value("\${security.token-expire-minute}")
    val tokenExpireMinute: Long? = null

    private val secretKey: SecretKey = Keys.secretKeyFor(SignatureAlgorithm.HS512)

    override fun generateToken(authentication: Authentication): String {
        val authorities = authentication.authorities.joinToString(",")
        return Jwts.builder()
                .setSubject(authentication.name)
                .claim("authorities", authorities)
                .claim("createdAt", Date())
                .signWith(secretKey, SignatureAlgorithm.HS512)
                .setExpiration(LocalDateTime.now().plusMinutes(tokenExpireMinute!!).toDateTime())
                .compact()
    }

    override fun getAuthentication(request: HttpServletRequest): Authentication? {
        val authHeader = request.getHeader("Authorization") ?: return null
        val jws = authHeader.replace("Bearer", "").trim()
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

        return UsernamePasswordAuthenticationToken(principal, credentials, authorities) // TODO: credentials 알아보기
    }
}