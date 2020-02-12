package com.mizelan.kotlinBoard.utils

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*

@Test
fun localDateTime_to_Date() {
    val localDateTime = LocalDateTime.of(2000, 1, 1, 1, 1)
    val localDateTime2 = localDateTime.toDateTime().toLocalDateTime()

    Assertions.assertEquals(localDateTime, localDateTime2)
}