package com.shishusneh.util

import org.junit.Assert.*
import org.junit.Test
import java.time.LocalDate

/**
 * Unit tests for AgeCalculator.
 */
class AgeCalculatorTest {

    @Test
    fun `newborn age is 0 days`() {
        val age = AgeCalculator.calculateAge(LocalDate.now(), LocalDate.now())
        assertEquals(0L, age.totalDays)
        assertEquals("Newborn", age.displayTextEn)
        assertEquals("नवजात", age.displayTextHi)
    }

    @Test
    fun `3 day old baby`() {
        val dob = LocalDate.now().minusDays(3)
        val age = AgeCalculator.calculateAge(dob, LocalDate.now())
        assertEquals(3L, age.totalDays)
        assertTrue(age.displayTextEn.contains("3 days"))
    }

    @Test
    fun `2 week old baby`() {
        val dob = LocalDate.now().minusDays(14)
        val age = AgeCalculator.calculateAge(dob, LocalDate.now())
        assertEquals(2L, age.totalWeeks)
        assertTrue(age.displayTextEn.contains("2 weeks"))
    }

    @Test
    fun `3 month old baby`() {
        val dob = LocalDate.now().minusMonths(3)
        val age = AgeCalculator.calculateAge(dob, LocalDate.now())
        assertEquals(3, age.months)
        assertTrue(age.displayTextEn.contains("3 months"))
    }

    @Test
    fun `fractional month calculation correct`() {
        val dob = LocalDate.now().minusDays(45)
        val ageMonths = AgeCalculator.getAgeInMonthsAtDate(
            dob.atStartOfDay(java.time.ZoneId.systemDefault()).toInstant().toEpochMilli(),
            System.currentTimeMillis()
        )
        assertTrue(ageMonths > 1.0 && ageMonths < 2.0) // ~1.5 months
    }
}
