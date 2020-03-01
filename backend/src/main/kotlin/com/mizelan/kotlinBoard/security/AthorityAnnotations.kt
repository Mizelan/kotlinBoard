package com.mizelan.kotlinBoard.security

import org.springframework.security.access.prepost.PreAuthorize

@kotlin.annotation.Target(AnnotationTarget.FUNCTION)
@kotlin.annotation.Retention(AnnotationRetention.RUNTIME)
@PreAuthorize("hasAuthority('USER')")
annotation class IsUser