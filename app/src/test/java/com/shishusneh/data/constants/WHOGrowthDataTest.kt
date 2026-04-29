package com.shishusneh.data.constants

import org.junit.Assert.*
import org.junit.Test

/**
 * Unit tests for WHO Growth Data calculations.
 */
class WHOGrowthDataTest {

    @Test
    fun `median weight for newborn boy is approximately 3_3kg`() {
        val percentile = WHOGrowthData.getWeightPercentile(3.35, 0, true)
        assertTrue(percentile in 45.0..55.0) // Should be near 50th percentile
    }

    @Test
    fun `very low weight triggers severe underweight zone`() {
        val percentile = WHOGrowthData.getWeightPercentile(2.0, 0, true)
        val zone = WHOGrowthData.getGrowthZone(percentile)
        assertEquals(WHOGrowthData.GrowthZone.SEVERE_UNDERWEIGHT, zone)
    }

    @Test
    fun `normal 6 month boy weight is healthy`() {
        val percentile = WHOGrowthData.getWeightPercentile(7.9, 6, true)
        val zone = WHOGrowthData.getGrowthZone(percentile)
        assertEquals(WHOGrowthData.GrowthZone.HEALTHY, zone)
    }

    @Test
    fun `percentile lines have data for 0 to 12 months`() {
        val lines = WHOGrowthData.getWeightPercentileLines(true)
        assertEquals(5, lines.size) // 3rd, 15th, 50th, 85th, 97th
        lines.values.forEach { points ->
            assertEquals(13, points.size) // 0–12 months
        }
    }

    @Test
    fun `girl and boy data are different`() {
        val boyP50 = WHOGrowthData.weightForAgeBoys[6].p50
        val girlP50 = WHOGrowthData.weightForAgeGirls[6].p50
        assertNotEquals(boyP50, girlP50) // Boys are typically heavier at 6 months
    }

    @Test
    fun `z-score calculation produces valid values`() {
        // For median value, z-score should be approximately 0
        val data = WHOGrowthData.weightForAgeBoys[6]
        val z = WHOGrowthData.calculateZScore(data.m, data.l, data.m, data.s)
        assertTrue(kotlin.math.abs(z) < 0.1)
    }

    @Test
    fun `percentile conversion is bounded`() {
        val lowPercentile = WHOGrowthData.zScoreToPercentile(-5.0)
        val highPercentile = WHOGrowthData.zScoreToPercentile(5.0)
        assertTrue(lowPercentile >= 0.0)
        assertTrue(highPercentile <= 100.0)
    }
}
