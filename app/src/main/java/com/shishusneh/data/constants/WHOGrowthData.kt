package com.shishusneh.data.constants

import kotlin.math.exp
import kotlin.math.ln
import kotlin.math.pow
import kotlin.math.sqrt

/**
 * WHO Child Growth Standards — Weight-for-Age data (0–12 months).
 * Source: WHO Multicentre Growth Reference Study Group (2006).
 * https://www.who.int/tools/child-growth-standards/standards/weight-for-age
 *
 * Uses the LMS method (Lambda-Mu-Sigma) for precise percentile calculation.
 * L = Box-Cox power, M = Median, S = Coefficient of variation.
 *
 * Percentile lines provided: 3rd, 15th, 50th, 85th, 97th.
 */
object WHOGrowthData {

    // =========================================================================
    // WEIGHT-FOR-AGE: BOYS (kg) — Monthly from 0 to 12 months
    // =========================================================================

    data class GrowthDataPoint(
        val monthAge: Int,
        val l: Double,   // Lambda (Box-Cox power)
        val m: Double,   // Mu (Median)
        val s: Double,   // Sigma (Coefficient of variation)
        // Pre-calculated percentiles for quick chart rendering
        val p3: Double,
        val p15: Double,
        val p50: Double,
        val p85: Double,
        val p97: Double
    )

    val weightForAgeBoys: List<GrowthDataPoint> = listOf(
        GrowthDataPoint(0,  0.3487, 3.3464, 0.14602, 2.5, 2.9, 3.3, 3.9, 4.4),
        GrowthDataPoint(1,  0.2297, 4.4709, 0.13395, 3.4, 3.9, 4.5, 5.1, 5.8),
        GrowthDataPoint(2,  0.1970, 5.5675, 0.12385, 4.3, 4.9, 5.6, 6.3, 7.1),
        GrowthDataPoint(3,  0.1738, 6.3762, 0.11727, 5.0, 5.7, 6.4, 7.2, 8.0),
        GrowthDataPoint(4,  0.1553, 7.0023, 0.11316, 5.6, 6.2, 7.0, 7.8, 8.7),
        GrowthDataPoint(5,  0.1395, 7.5105, 0.10990, 6.0, 6.7, 7.5, 8.4, 9.3),
        GrowthDataPoint(6,  0.1257, 7.9340, 0.10652, 6.4, 7.1, 7.9, 8.8, 9.8),
        GrowthDataPoint(7,  0.1134, 8.2970, 0.10350, 6.7, 7.4, 8.3, 9.2, 10.2),
        GrowthDataPoint(8,  0.1021, 8.6151, 0.10089, 7.0, 7.7, 8.6, 9.6, 10.5),
        GrowthDataPoint(9,  0.0917, 8.9014, 0.09867, 7.2, 7.9, 8.9, 9.9, 10.9),
        GrowthDataPoint(10, 0.0820, 9.1649, 0.09681, 7.4, 8.2, 9.2, 10.2, 11.2),
        GrowthDataPoint(11, 0.0730, 9.4122, 0.09524, 7.6, 8.4, 9.4, 10.5, 11.5),
        GrowthDataPoint(12, 0.0644, 9.6479, 0.09394, 7.7, 8.6, 9.6, 10.8, 11.8)
    )

    // =========================================================================
    // WEIGHT-FOR-AGE: GIRLS (kg) — Monthly from 0 to 12 months
    // =========================================================================

    val weightForAgeGirls: List<GrowthDataPoint> = listOf(
        GrowthDataPoint(0,  0.3809, 3.2322, 0.14171, 2.4, 2.8, 3.2, 3.7, 4.2),
        GrowthDataPoint(1,  0.1714, 4.1873, 0.13724, 3.2, 3.6, 4.2, 4.8, 5.5),
        GrowthDataPoint(2,  0.0962, 5.1282, 0.12960, 3.9, 4.5, 5.1, 5.8, 6.6),
        GrowthDataPoint(3,  0.0402, 5.8458, 0.12619, 4.5, 5.2, 5.8, 6.6, 7.5),
        GrowthDataPoint(4, -0.0050, 6.4237, 0.12402, 5.0, 5.7, 6.4, 7.3, 8.2),
        GrowthDataPoint(5, -0.0430, 6.8985, 0.12274, 5.4, 6.1, 6.9, 7.8, 8.8),
        GrowthDataPoint(6, -0.0750, 7.2970, 0.12204, 5.7, 6.5, 7.3, 8.2, 9.3),
        GrowthDataPoint(7, -0.1020, 7.6422, 0.12178, 6.0, 6.8, 7.6, 8.6, 9.8),
        GrowthDataPoint(8, -0.1240, 7.9487, 0.12181, 6.3, 7.0, 7.9, 9.0, 10.2),
        GrowthDataPoint(9, -0.1420, 8.2254, 0.12199, 6.5, 7.3, 8.2, 9.3, 10.5),
        GrowthDataPoint(10,-0.1570, 8.4800, 0.12223, 6.7, 7.5, 8.5, 9.6, 10.9),
        GrowthDataPoint(11,-0.1700, 8.7192, 0.12247, 6.9, 7.7, 8.7, 9.9, 11.2),
        GrowthDataPoint(12,-0.1810, 8.9481, 0.12268, 7.0, 7.9, 8.9, 10.1, 11.5)
    )

    // =========================================================================
    // HEIGHT/LENGTH-FOR-AGE: BOYS (cm) — Monthly from 0 to 12 months
    // =========================================================================

    data class HeightDataPoint(
        val monthAge: Int,
        val p3: Double,
        val p15: Double,
        val p50: Double,
        val p85: Double,
        val p97: Double
    )

    val heightForAgeBoys: List<HeightDataPoint> = listOf(
        HeightDataPoint(0,  46.1, 47.9, 49.9, 51.8, 53.7),
        HeightDataPoint(1,  50.8, 52.8, 54.7, 56.7, 58.6),
        HeightDataPoint(2,  54.4, 56.4, 58.4, 60.4, 62.4),
        HeightDataPoint(3,  57.3, 59.4, 61.4, 63.5, 65.5),
        HeightDataPoint(4,  59.7, 61.8, 63.9, 66.0, 68.0),
        HeightDataPoint(5,  61.7, 63.8, 65.9, 68.0, 70.1),
        HeightDataPoint(6,  63.3, 65.5, 67.6, 69.8, 71.9),
        HeightDataPoint(7,  64.8, 67.0, 69.2, 71.3, 73.5),
        HeightDataPoint(8,  66.2, 68.4, 70.6, 72.8, 75.0),
        HeightDataPoint(9,  67.5, 69.7, 72.0, 74.2, 76.5),
        HeightDataPoint(10, 68.7, 71.0, 73.3, 75.6, 77.9),
        HeightDataPoint(11, 69.9, 72.2, 74.5, 76.9, 79.2),
        HeightDataPoint(12, 71.0, 73.4, 75.7, 78.1, 80.5)
    )

    // =========================================================================
    // HEIGHT/LENGTH-FOR-AGE: GIRLS (cm) — Monthly from 0 to 12 months
    // =========================================================================

    val heightForAgeGirls: List<HeightDataPoint> = listOf(
        HeightDataPoint(0,  45.4, 47.3, 49.1, 51.0, 52.9),
        HeightDataPoint(1,  49.8, 51.7, 53.7, 55.6, 57.6),
        HeightDataPoint(2,  53.0, 55.0, 57.1, 59.1, 61.1),
        HeightDataPoint(3,  55.6, 57.7, 59.8, 61.9, 64.0),
        HeightDataPoint(4,  57.8, 59.9, 62.1, 64.3, 66.4),
        HeightDataPoint(5,  59.6, 61.8, 64.0, 66.2, 68.5),
        HeightDataPoint(6,  61.2, 63.5, 65.7, 68.0, 70.3),
        HeightDataPoint(7,  62.7, 65.0, 67.3, 69.6, 71.9),
        HeightDataPoint(8,  64.0, 66.4, 68.7, 71.1, 73.5),
        HeightDataPoint(9,  65.3, 67.7, 70.1, 72.6, 75.0),
        HeightDataPoint(10, 66.5, 69.0, 71.5, 73.9, 76.4),
        HeightDataPoint(11, 67.7, 70.3, 72.8, 75.3, 77.8),
        HeightDataPoint(12, 68.9, 71.4, 74.0, 76.6, 79.2)
    )

    // =========================================================================
    // UTILITY FUNCTIONS
    // =========================================================================

    /**
     * Calculate z-score using WHO LMS method.
     * z = ((value/M)^L - 1) / (L * S)   when L ≠ 0
     * z = ln(value/M) / S                when L = 0
     */
    fun calculateZScore(value: Double, l: Double, m: Double, s: Double): Double {
        return if (l != 0.0) {
            ((value / m).pow(l) - 1.0) / (l * s)
        } else {
            ln(value / m) / s
        }
    }

    /**
     * Convert z-score to percentile using standard normal CDF approximation.
     * Uses the Abramowitz and Stegun approximation (error < 1.5×10⁻⁷).
     */
    fun zScoreToPercentile(z: Double): Double {
        // Constants for approximation
        val a1 = 0.254829592
        val a2 = -0.284496736
        val a3 = 1.421413741
        val a4 = -1.453152027
        val a5 = 1.061405429
        val p = 0.3275911

        val sign = if (z < 0) -1.0 else 1.0
        val absZ = Math.abs(z)
        val t = 1.0 / (1.0 + p * absZ)
        val y = 1.0 - (((((a5 * t + a4) * t) + a3) * t + a2) * t + a1) * t * exp(-absZ * absZ / 2.0)

        return ((0.5 * (1.0 + sign * y)) * 100.0).coerceIn(0.0, 100.0)
    }

    /**
     * Get weight-for-age percentile for a baby.
     * @param weightKg Baby's weight in kg
     * @param ageMonths Baby's age in months (0–12)
     * @param isMale true for boys, false for girls
     * @return Percentile value (0–100)
     */
    fun getWeightPercentile(weightKg: Double, ageMonths: Int, isMale: Boolean): Double {
        val data = if (isMale) weightForAgeBoys else weightForAgeGirls
        val point = data.getOrNull(ageMonths.coerceIn(0, 12)) ?: return 50.0
        val zScore = calculateZScore(weightKg, point.l, point.m, point.s)
        return zScoreToPercentile(zScore)
    }

    /**
     * Determine growth zone based on percentile.
     */
    enum class GrowthZone {
        SEVERE_UNDERWEIGHT,  // < 3rd percentile
        UNDERWEIGHT,         // 3rd–15th percentile
        HEALTHY,             // 15th–85th percentile
        OVERWEIGHT,          // 85th–97th percentile
        OBESE                // > 97th percentile
    }

    fun getGrowthZone(percentile: Double): GrowthZone {
        return when {
            percentile < 3.0 -> GrowthZone.SEVERE_UNDERWEIGHT
            percentile < 15.0 -> GrowthZone.UNDERWEIGHT
            percentile < 85.0 -> GrowthZone.HEALTHY
            percentile < 97.0 -> GrowthZone.OVERWEIGHT
            else -> GrowthZone.OBESE
        }
    }

    /**
     * Get the weight data points for chart percentile lines.
     * Returns a map of percentile label → list of (month, weight) pairs.
     */
    fun getWeightPercentileLines(isMale: Boolean): Map<String, List<Pair<Int, Double>>> {
        val data = if (isMale) weightForAgeBoys else weightForAgeGirls
        return mapOf(
            "3rd" to data.map { it.monthAge to it.p3 },
            "15th" to data.map { it.monthAge to it.p15 },
            "50th" to data.map { it.monthAge to it.p50 },
            "85th" to data.map { it.monthAge to it.p85 },
            "97th" to data.map { it.monthAge to it.p97 }
        )
    }

    /**
     * Get the height data points for chart percentile lines.
     */
    fun getHeightPercentileLines(isMale: Boolean): Map<String, List<Pair<Int, Double>>> {
        val data = if (isMale) heightForAgeBoys else heightForAgeGirls
        return mapOf(
            "3rd" to data.map { it.monthAge to it.p3 },
            "15th" to data.map { it.monthAge to it.p15 },
            "50th" to data.map { it.monthAge to it.p50 },
            "85th" to data.map { it.monthAge to it.p85 },
            "97th" to data.map { it.monthAge to it.p97 }
        )
    }
}
