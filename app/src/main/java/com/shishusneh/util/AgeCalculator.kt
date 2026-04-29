package com.shishusneh.util

import java.time.Instant
import java.time.LocalDate
import java.time.Period
import java.time.ZoneId
import java.time.temporal.ChronoUnit

/**
 * Utility for calculating baby's age in various units.
 * Handles leap years and month boundaries correctly.
 */
object AgeCalculator {

    data class BabyAge(
        val totalDays: Long,
        val totalWeeks: Long,
        val months: Int,
        val weeks: Int,       // Remaining weeks after months
        val days: Int,        // Remaining days after weeks
        val displayTextEn: String,
        val displayTextHi: String
    )

    /**
     * Calculate baby's age from DOB (epoch millis) to now.
     */
    fun calculateAge(dobMillis: Long): BabyAge {
        val dob = Instant.ofEpochMilli(dobMillis).atZone(ZoneId.systemDefault()).toLocalDate()
        val today = LocalDate.now()
        return calculateAge(dob, today)
    }

    /**
     * Calculate baby's age between two dates.
     */
    fun calculateAge(dob: LocalDate, asOf: LocalDate): BabyAge {
        val totalDays = ChronoUnit.DAYS.between(dob, asOf).coerceAtLeast(0)
        val totalWeeks = totalDays / 7
        val period = Period.between(dob, asOf)
        val months = (period.years * 12 + period.months).coerceAtLeast(0)

        // Calculate remaining weeks/days after whole months
        val monthStart = dob.plusMonths(months.toLong())
        val daysAfterMonth = ChronoUnit.DAYS.between(monthStart, asOf).coerceAtLeast(0)
        val remainingWeeks = (daysAfterMonth / 7).toInt()
        val remainingDays = (daysAfterMonth % 7).toInt()

        val displayEn = formatAgeEnglish(months, remainingWeeks, totalWeeks, totalDays)
        val displayHi = formatAgeHindi(months, remainingWeeks, totalWeeks, totalDays)

        return BabyAge(
            totalDays = totalDays,
            totalWeeks = totalWeeks,
            months = months,
            weeks = remainingWeeks,
            days = remainingDays,
            displayTextEn = displayEn,
            displayTextHi = displayHi
        )
    }

    /**
     * Get age in months (fractional) for WHO growth chart calculations.
     */
    fun getAgeInMonths(dobMillis: Long): Double {
        val totalDays = ChronoUnit.DAYS.between(
            Instant.ofEpochMilli(dobMillis).atZone(ZoneId.systemDefault()).toLocalDate(),
            LocalDate.now()
        ).coerceAtLeast(0)
        return totalDays / 30.4375  // Average days per month
    }

    /**
     * Get age in months at a specific measurement date.
     */
    fun getAgeInMonthsAtDate(dobMillis: Long, dateMillis: Long): Double {
        val totalDays = ChronoUnit.DAYS.between(
            Instant.ofEpochMilli(dobMillis).atZone(ZoneId.systemDefault()).toLocalDate(),
            Instant.ofEpochMilli(dateMillis).atZone(ZoneId.systemDefault()).toLocalDate()
        ).coerceAtLeast(0)
        return totalDays / 30.4375
    }

    private fun formatAgeEnglish(months: Int, weeks: Int, totalWeeks: Long, totalDays: Long): String {
        return when {
            totalDays == 0L -> "Newborn"
            totalDays < 7 -> "$totalDays day${if (totalDays != 1L) "s" else ""} old"
            months == 0 -> "$totalWeeks week${if (totalWeeks != 1L) "s" else ""} old"
            weeks > 0 -> "$months month${if (months != 1) "s" else ""}, $weeks week${if (weeks != 1) "s" else ""} old"
            else -> "$months month${if (months != 1) "s" else ""} old"
        }
    }

    private fun formatAgeHindi(months: Int, weeks: Int, totalWeeks: Long, totalDays: Long): String {
        return when {
            totalDays == 0L -> "नवजात"
            totalDays < 7 -> "$totalDays दिन का"
            months == 0 -> "$totalWeeks सप्ताह का"
            weeks > 0 -> "$months महीने, $weeks सप्ताह का"
            else -> "$months महीने का"
        }
    }
}
