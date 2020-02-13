package com.mizelan.kotlinBoard.security

import com.mizelan.kotlinBoard.security.jwt.JwtAuthenticationFilter
import com.mizelan.kotlinBoard.security.jwt.JwtProvider
import com.mizelan.kotlinBoard.security.jwt.JwtProviderImpl
import com.mizelan.kotlinBoard.security.service.SimpleUserDetailsService
import com.mizelan.kotlinBoard.user.UserRole
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.core.AuthenticationException
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse


@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
class SecurityConfig : WebSecurityConfigurerAdapter() {
    @Bean
    fun jwtProvider() = JwtProviderImpl()

    @Autowired
    private lateinit var simpleUserDetailsService: SimpleUserDetailsService

    @Throws(Exception::class)
    override fun configure(http: HttpSecurity) {
        http.cors();
        http.csrf().disable();
        http.headers().frameOptions().disable()
        http.exceptionHandling().authenticationEntryPoint {
            req: HttpServletRequest?,
            rsp: HttpServletResponse,
            e: AuthenticationException? -> rsp.sendError(HttpServletResponse.SC_UNAUTHORIZED)
        }

        http.addFilterBefore(JwtAuthenticationFilter(jwtProvider()),
                UsernamePasswordAuthenticationFilter::class.java);
        http.authorizeRequests()
                .antMatchers("/", "/css/**", "/js/**", "/favicon.ico").permitAll()
                .antMatchers(HttpMethod.POST, "/api/user/**").permitAll()
                .antMatchers("/api/post/**").hasAnyRole(UserRole.USER.name, UserRole.ADMIN.name)
                .antMatchers("/api/admin/**").hasAnyRole(UserRole.ADMIN.name)
                .anyRequest().authenticated()
    }

    override fun userDetailsService(): UserDetailsService {
        return simpleUserDetailsService
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }
}