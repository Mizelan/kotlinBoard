package com.mizelan.kotlinBoard.feature_tests

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*
import org.springframework.boot.test.context.SpringBootTest
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.temporal.TemporalUnit
import java.util.*

class TimeTests {

    @Test
    fun localDateTime_plusMinute() {
        val now = LocalDateTime.of(2000, 1, 1, 1, 1)
        val after = now.plusMinutes(1)

        assertEquals(LocalDateTime.of(2000, 1, 1, 1, 2), after)
    }
}
