package com.mizelan.kotlinBoard.feature_tests

import com.mizelan.kotlinBoard.utils.toDateTime
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.security.Keys
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*
import java.time.LocalDateTime
import javax.crypto.SecretKey

class JwtTests {
    @Test
    fun build_and_parsing() {
        val secretKey: SecretKey = Keys.secretKeyFor(SignatureAlgorithm.HS512)
        val subjectName = "subject"
        val createdAt = LocalDateTime.of(2000, 1, 1, 1, 1)
        var jws = Jwts.builder()
                .setSubject(subjectName)
                .claim("createdAt", createdAt.toDateTime())
                .signWith(secretKey, SignatureAlgorithm.HS512)
                .setExpiration(LocalDateTime.now().plusMinutes(1).toDateTime())
                .compact()

        val jwtsParser = Jwts.parserBuilder().setSigningKey(secretKey).build();
        val claims = jwtsParser.parseClaimsJws(jws).body;

        assertEquals(subjectName, claims.subject)
        assertEquals(createdAt.toDateTime().time, claims["createdAt"])
    }
}