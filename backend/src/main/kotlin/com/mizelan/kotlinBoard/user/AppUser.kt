package com.mizelan.kotlinBoard.user

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.User

class AppUser(
        val userId: Long,
        username: String,
        password: String,
        authorities: List<GrantedAuthority>
): User(username, password, authorities)