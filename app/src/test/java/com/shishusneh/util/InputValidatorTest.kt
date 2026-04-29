package com.shishusneh.util

import org.junit.Assert.*
import org.junit.Test

/**
 * Unit tests for InputValidator — covers all edge cases.
 */
class InputValidatorTest {

    @Test
    fun `valid baby name passes`() {
        assertTrue(InputValidator.validateBabyName("Arya").isValid)
        assertTrue(InputValidator.validateBabyName("Arundhati Sharma").isValid)
        assertTrue(InputValidator.validateBabyName("O'Brien").isValid)
    }

    @Test
    fun `empty baby name fails`() {
        assertFalse(InputValidator.validateBabyName("").isValid)
        assertFalse(InputValidator.validateBabyName("   ").isValid)
    }

    @Test
    fun `single char baby name fails`() {
        assertFalse(InputValidator.validateBabyName("A").isValid)
    }

    @Test
    fun `very long baby name fails`() {
        assertFalse(InputValidator.validateBabyName("A".repeat(51)).isValid)
    }

    @Test
    fun `name with special characters fails`() {
        assertFalse(InputValidator.validateBabyName("Baby@123").isValid)
        assertFalse(InputValidator.validateBabyName("Baby<script>").isValid)
    }

    @Test
    fun `valid birth weight passes`() {
        assertTrue(InputValidator.validateBirthWeight(2.8f).isValid)
        assertTrue(InputValidator.validateBirthWeight(3.5f).isValid)
        assertTrue(InputValidator.validateBirthWeight(0.6f).isValid)
    }

    @Test
    fun `zero birth weight fails`() {
        assertFalse(InputValidator.validateBirthWeight(0f).isValid)
    }

    @Test
    fun `very low birth weight warns`() {
        assertFalse(InputValidator.validateBirthWeight(0.3f).isValid)
    }

    @Test
    fun `very high birth weight fails`() {
        assertFalse(InputValidator.validateBirthWeight(7.0f).isValid)
    }

    @Test
    fun `valid weight passes`() {
        assertTrue(InputValidator.validateWeight(3.5f).isValid)
        assertTrue(InputValidator.validateWeight(10.0f).isValid)
    }

    @Test
    fun `negative weight fails`() {
        assertFalse(InputValidator.validateWeight(-1f).isValid)
    }

    @Test
    fun `unrealistic baby weight fails`() {
        assertFalse(InputValidator.validateWeight(30f).isValid)
    }

    @Test
    fun `valid height passes`() {
        assertTrue(InputValidator.validateHeight(50f).isValid)
        assertTrue(InputValidator.validateHeight(75f).isValid)
    }

    @Test
    fun `extreme height fails`() {
        assertFalse(InputValidator.validateHeight(15f).isValid)
        assertFalse(InputValidator.validateHeight(130f).isValid)
    }

    @Test
    fun `future DOB fails`() {
        val futureDate = System.currentTimeMillis() + 86400000L // Tomorrow
        assertFalse(InputValidator.validateDOB(futureDate).isValid)
    }

    @Test
    fun `recent DOB passes`() {
        val recentDate = System.currentTimeMillis() - 86400000L // Yesterday
        assertTrue(InputValidator.validateDOB(recentDate).isValid)
    }

    @Test
    fun `very old DOB fails`() {
        val threeYearsAgo = System.currentTimeMillis() - (3L * 365 * 24 * 60 * 60 * 1000)
        assertFalse(InputValidator.validateDOB(threeYearsAgo).isValid)
    }

    @Test
    fun `suspicious weight gain detected`() {
        val result = InputValidator.checkWeightChange(5.0f, 8.0f, 15)
        assertFalse(result.isValid) // 3kg in 15 days = 6kg/month
    }

    @Test
    fun `normal weight gain passes`() {
        val result = InputValidator.checkWeightChange(5.0f, 5.5f, 30)
        assertTrue(result.isValid) // 0.5kg in 30 days = normal
    }

    @Test
    fun `significant weight loss detected`() {
        val result = InputValidator.checkWeightChange(6.0f, 4.5f, 30)
        assertFalse(result.isValid) // 25% loss
    }

    @Test
    fun `measurement date before DOB fails`() {
        val dob = System.currentTimeMillis() - 86400000L
        val measureDate = dob - 86400000L
        assertFalse(InputValidator.validateMeasurementDate(measureDate, dob).isValid)
    }

    @Test
    fun `future measurement date fails`() {
        val dob = System.currentTimeMillis() - 86400000L
        val futureDate = System.currentTimeMillis() + 86400000L
        assertFalse(InputValidator.validateMeasurementDate(futureDate, dob).isValid)
    }
}
