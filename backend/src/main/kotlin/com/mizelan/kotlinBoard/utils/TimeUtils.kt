package com.mizelan.kotlinBoard.utils

import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*

fun LocalDateTime.toDateTime() = Date.from(this.atZone(ZoneId.systemDefault()).toInstant())
fun Date.toLocalDateTime() = LocalDateTime.ofInstant(this.toInstant(), ZoneId.systemDefault())